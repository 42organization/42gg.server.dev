package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamModifyGameResultDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.global.util.EloRating;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class GameControllerImpl implements GameController {
    private final GameService gameService;
    private final TeamService teamService;
    private final UserService userService;
    private final PChangeService pChangeService;

    @Override
    @GetMapping(value = "/games/{gameId}/result")
    public GameUserInfoResponseDto gameUserInfo(Integer gameId, Integer userId) {
        GameDto game = gameService.findById(gameId);
        UserDto user = userService.findById(userId);
        List<GameUserInfoDto> myTeams = new ArrayList<>();
        List<GameUserInfoDto> enemyTeams = new ArrayList<>();

        //figuring out team number for myteam and enemyteam, and put each team's user infos in the right team
        putUsersDataInTeams(game, user, myTeams, enemyTeams);

        //make Dto to return
        GameUserInfoResponseDto gameUserInfoResponseDto = GameUserInfoResponseDto.builder()
                .myTeam(myTeams)
                .enemyTeam(enemyTeams)
                .build();
        return gameUserInfoResponseDto;
    }

    @Override
    @PostMapping(value = "/games/{gameId}/result")
    public void gameResultSave(Integer gameId, GameResultRequestDto requestDto, Integer userId) {
        GameDto game = gameService.findById(gameId);
        UserDto user = userService.findById(userId);
        TeamDto team1 = game.getTeam1();
        TeamDto team2 = game.getTeam2();
        //figuring out team number for myteam and enemyteam
        List<TeamModifyGameResultDto> eachTeamModifyDto = getTeamModifyDto(team1, team2, requestDto, user);
        //modify team with game result
        for (TeamModifyGameResultDto dto : eachTeamModifyDto) {
            teamService.modifyGameResultInTeam(dto);
        }
        //modify users with game result
        TeamDto savedTeam1 = teamService.findById(team1.getId());
        TeamDto savedTeam2 = teamService.findById(team2.getId());
        modifyUserPppAndAddPchange(game.getId(), team1.getUser1(), savedTeam2);
        modifyUserPppAndAddPchange(game.getId(), team1.getUser2(), savedTeam2);
        modifyUserPppAndAddPchange(game.getId(), team2.getUser1(), savedTeam1);
        modifyUserPppAndAddPchange(game.getId(), team2.getUser2(), savedTeam1);

        //modify users' ranks with game result
    }

    @Override
    @GetMapping(value = "/games")
    public GameResultResponseDto gameResultByIndexAndCount(Pageable pageable, String status) {
        GameResultPageDto resultPageDto = gameService.findEndGames(pageable);
        TeamDto team1;
        TeamDto team2;
        List<GamePlayerDto> gamePlayerList;
        List<GameResultDto> gameResultList = new ArrayList<>();
        GameTeamDto teamDto1;
        GameTeamDto teamDto2;
        List<GameDto> gameLists = resultPageDto.getGameList();

        for (GameDto game : gameLists) {
            team1 = game.getTeam1();
            team2 = game.getTeam2();

            gamePlayerList = new ArrayList<>();
            putGamePlayerDto(game, team1.getUser1(), gamePlayerList);
            putGamePlayerDto(game, team1.getUser2(), gamePlayerList);
            teamDto1 = GameTeamDto.builder()
                    .isWin(team1.getWin())
                    .score(team1.getScore())
                    .playerInfos(gamePlayerList)
                    .build();

            gamePlayerList = new ArrayList();
            putGamePlayerDto(game, team2.getUser1(), gamePlayerList);
            putGamePlayerDto(game, team2.getUser2(), gamePlayerList);
            teamDto2 = GameTeamDto.builder()
                    .isWin(team2.getWin())
                    .score(team2.getScore())
                    .playerInfos(gamePlayerList)
                    .build();

            gameResultList.add(GameResultDto.builder()
                    .gameId(game.getId())
                    .team1(teamDto1)
                    .team2(teamDto2)
                    .status(game.getStatus())
                    .time(game.getTime())
                    .build());
        }

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .currentPage(resultPageDto.getCurrentPage())
                .totalPage(resultPageDto.getTotalPage())
                .build();
        return gameResultResponse;
    }

    private void putGamePlayerDto(GameDto game, UserDto user, List<GamePlayerDto> gamePlayerList) {
        if (user == null) {
            return;
        } else {
            PChangeDto pChangeDto = pChangeService.findPChangeByUserAndGame(PChangeFindDto.builder().gameId(game.getId()).userId(user.getIntraId()).build());
            GamePlayerDto gamePlayerDto = GamePlayerDto.builder()
                    .userId(user.getIntraId())
                    .userImageUri(user.getImageUri())
                    .wins(null)
                    .losses(null)
                    .winRate(null)
                    .pppChange(pChangeDto.getPppChange())
                    .build();
            gamePlayerList.add(gamePlayerDto);
        }
    }

    @Override
    @GetMapping(value = "/games/{userId}")
    public GameResultResponseDto gameResultByUserIdAndIndexAndCount(String userId, Pageable pageable, String type) {
        //Pageable
        /*
         * 1. PChange에서 유저별 게임 목록을 가져온다
         * 2. 얘네를 바탕으로 게임을 다 긁어온다.
         * 3. 얘네를 DTO로 만들어준다
         *
         */
        PChangeFindDto findDto = PChangeFindDto.builder()
                .userId(userId)
                .build();
        PChangePageDto pChangePageDto = pChangeService.findPChangeByUserId(findDto, pageable);
        List<PChangeDto> gameLists = pChangePageDto.getPChangeList();
        TeamDto team1;
        TeamDto team2;
        List<GamePlayerDto> gamePlayerList;
        List<GameResultDto> gameResultList = new ArrayList<>();
        GameTeamDto teamDto1;
        GameTeamDto teamDto2;

        for (PChangeDto pChangeDto : gameLists) {
            GameDto game = pChangeDto.getGame();
            team1 = game.getTeam1();
            team2 = game.getTeam2();

            gamePlayerList = new ArrayList<>();
            putGamePlayerDto(game, team1.getUser1(), gamePlayerList);
            putGamePlayerDto(game, team1.getUser2(), gamePlayerList);
            teamDto1 = GameTeamDto.builder()
                    .isWin(team1.getWin())
                    .score(team1.getScore())
                    .playerInfos(gamePlayerList)
                    .build();

            gamePlayerList = new ArrayList();
            putGamePlayerDto(game, team2.getUser1(), gamePlayerList);
            putGamePlayerDto(game, team2.getUser2(), gamePlayerList);
            teamDto2 = GameTeamDto.builder()
                    .isWin(team2.getWin())
                    .score(team2.getScore())
                    .playerInfos(gamePlayerList)
                    .build();

            gameResultList.add(GameResultDto.builder()
                    .gameId(game.getId())
                    .team1(teamDto1)
                    .team2(teamDto2)
                    .status(game.getStatus())
                    .time(game.getTime())
                    .build());
        }
        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .currentPage(pChangePageDto.getCurrentPage())
                .totalPage(pChangePageDto.getTotalPage())
                .build();
        return gameResultResponse;
    }



    private List<TeamModifyGameResultDto> getTeamModifyDto(TeamDto team1, TeamDto team2, GameResultRequestDto requestDto, UserDto user) {
        Integer team1Score;
        Integer team2Score;
        if (user.equals(team1.getUser1()) || user.equals(team1.getUser2())) {
            team1Score = requestDto.getMyTeamScore();
            team2Score = requestDto.getEnemyTeamScore();
        } else if (user.equals(team2.getUser1()) || user.equals(team2.getUser2())) {
            team2Score = requestDto.getMyTeamScore();
            team1Score = requestDto.getEnemyTeamScore();
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        List<TeamModifyGameResultDto> modifyList = new ArrayList<>();
        modifyList.add(TeamModifyGameResultDto.builder()
                .teamId(team1.getId())
                .score(team1Score)
                .win(team1Score > team2Score)
                .build()
        );
        modifyList.add(TeamModifyGameResultDto.builder()
                .teamId(team2.getId())
                .score(team2Score)
                .win(team2Score > team1Score)
                .build()
        );
        return modifyList;
    }

    private void modifyUserPppAndAddPchange(Integer gameId, UserDto user, TeamDto enemyTeam) {
        if (user == null) {
            return;
        }
        Integer pppChange = EloRating.pppChange(user.getPpp(), enemyTeam.getTeamPpp(), !enemyTeam.getWin());
        UserModifyPppDto modifyPppDto = UserModifyPppDto.builder()
                .userId(user.getId())
                .ppp(pppChange)
                .build();
        userService.modifyUserPpp(modifyPppDto);
        PChangeAddDto pChangeAddDto = PChangeAddDto.builder()
                .gameId(gameId)
                .userId(user.getId())
                .pppChange(pppChange)
                .pppResult(user.getPpp() + pppChange)
                .build();
        pChangeService.addPChange(pChangeAddDto);
    }

    private void putUsersDataInTeams(GameDto game, UserDto user, List<GameUserInfoDto> myTeams, List<GameUserInfoDto> enemyTeams) {
        TeamDto team1 = game.getTeam1();
        TeamDto team2 = game.getTeam2();
        TeamDto myTeam;
        TeamDto enemyTeam;

        //figuring out team number for myteam and enemyteam
        if (user.equals(team1.getUser1()) || user.equals(team1.getUser2())) {
            myTeam = team1;
            enemyTeam = team2;
        } else if (user.equals(team2.getUser1()) || user.equals(team2.getUser2())) {
            myTeam = team2;
            enemyTeam = team1;
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        UserDto myTeamUser1 = myTeam.getUser1();
        UserDto myTeamUser2 = myTeam.getUser2();
        UserDto enemyTeamUser1 = enemyTeam.getUser1();
        UserDto enemyTeamUser2 = enemyTeam.getUser2();
        if (myTeamUser1 != null) {
            myTeams.add(GameUserInfoDto.builder().intraId(myTeamUser1.getIntraId()).userImageUri(myTeamUser1.getImageUri()).build());
        }
        if (myTeamUser2 != null) {
            myTeams.add(GameUserInfoDto.builder().intraId(myTeamUser2.getIntraId()).userImageUri(myTeamUser2.getImageUri()).build());
        }
        if (enemyTeamUser1 != null) {
            enemyTeams.add(GameUserInfoDto.builder().intraId(enemyTeamUser1.getIntraId()).userImageUri(enemyTeamUser1.getImageUri()).build());
        }
        if (enemyTeamUser2 != null) {
            enemyTeams.add(GameUserInfoDto.builder().intraId(enemyTeamUser2.getIntraId()).userImageUri(enemyTeamUser2.getImageUri()).build());
        }
    }
}
