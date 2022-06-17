package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchFindDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamModifyGameResultDto;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.global.util.EloRating;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class GameControllerImpl implements GameController {
    private final GameService gameService;
    private final TeamService teamService;
    private final UserService userService;
    private final PChangeService pChangeService;
    private final CurrentMatchService currentMatchService;

    @Override
    @GetMapping(value = "/games/{gameId}/result")
    public GameUserInfoResponseDto gameUserInfo(Integer gameId, Integer userId) {
        GameDto game = gameService.findById(gameId);
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
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
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        TeamDto team1 = game.getTeam1();
        TeamDto team2 = game.getTeam2();
        // if the result already exists, throw 202 error
        checkIfResultExists(game);
        //figuring out team number for myteam and enemyteam
        List<TeamModifyGameResultDto> eachTeamModifyDto = getTeamModifyDto(team1, team2, requestDto, user);
        //modify team with game result
        for (TeamModifyGameResultDto dto : eachTeamModifyDto) {
            teamService.modifyGameResultInTeam(dto);
        }
        //modify users with game result
        modifyUsersPppAndPChange(game, team1, team2);
        endGameStatus(game);
        removCurrentMatch(game);
        //modify users' ranks with game result
    }

    @Override
    @GetMapping(value = "/games")
    public GameResultResponseDto gameResultByIndexAndCount(Pageable pageable, String status) {
        GameResultPageDto resultPageDto = gameService.findEndGames(pageable);
        List<GameResultDto> gameResultList = new ArrayList<>();
        List<GameDto> gameLists = resultPageDto.getGameList();

        putResultInGames(gameResultList, gameLists);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .currentPage(resultPageDto.getCurrentPage())
                .totalPage(resultPageDto.getTotalPage())
                .build();
        return gameResultResponse;
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
        List<PChangeDto> pChangeLists = pChangePageDto.getPChangeList();
        List<GameDto> gameLists = pChangeLists.stream().map(PChangeDto::getGame).collect(Collectors.toList());;
        List<GameResultDto> gameResultList = new ArrayList<>();

        putResultInGames(gameResultList, gameLists);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .currentPage(pChangePageDto.getCurrentPage())
                .totalPage(pChangePageDto.getTotalPage())
                .build();
        return gameResultResponse;
    }


    // 이하 분리된 메서드
    private void checkIfResultExists(GameDto game) {
        if ("end".equals(game.getStatus()))
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "이미 입력해짜나~~");
    }

    private void endGameStatus(GameDto game) {
        GameModifyStatusDto modifyStatusDto = GameModifyStatusDto.builder()
                .gameId(game.getId())
                .status("end").build();
        gameService.modifyGameStatus(modifyStatusDto);
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

    private void modifyUsersPppAndPChange(GameDto game, TeamDto team1, TeamDto team2) {
        TeamDto savedTeam1 = teamService.findById(team1.getId());
        TeamDto savedTeam2 = teamService.findById(team2.getId());
        modifyUserPppAndAddPchange(game.getId(), team1.getUser1(), savedTeam2);
        modifyUserPppAndAddPchange(game.getId(), team1.getUser2(), savedTeam2);
        modifyUserPppAndAddPchange(game.getId(), team2.getUser1(), savedTeam1);
        modifyUserPppAndAddPchange(game.getId(), team2.getUser2(), savedTeam1);
    }
    
    private void modifyUserPppAndAddPchange(Integer gameId, UserDto user, TeamDto enemyTeam) {
        if (user == null) {
            return;
        }
        Integer pppChange = EloRating.pppChange(user.getPpp(), enemyTeam.getTeamPpp(), !enemyTeam.getWin());
        UserModifyPppDto modifyPppDto = UserModifyPppDto.builder()
                .userId(user.getId())
                .ppp(user.getPpp() + pppChange)
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

    private void removCurrentMatch(GameDto game) {
        CurrentMatchFindDto findDto = CurrentMatchFindDto.builder()
                .game(game)
                .build();
        List<CurrentMatchDto> currentMatchDtos = currentMatchService.findCurrentMatchByGame(findDto);
        currentMatchDtos.forEach(currentMatchDto -> {
            CurrentMatchRemoveDto removeDto = CurrentMatchRemoveDto.builder()
                    .userId(currentMatchDto.getUserId()).build();
            currentMatchService.removeCurrentMatch(removeDto);
        });
    }

    private void putUsersDataInTeams(GameDto game, UserDto user, List<GameUserInfoDto> myTeams, List<GameUserInfoDto> enemyTeams) {
        //figuring out team number for myteam and enemyteam
        TeamPosDto teamPos = teamService.getTeamPosNT(user, game.getTeam1(), game.getTeam2());

        TeamDto myTeam = teamPos.getMyTeam();
        TeamDto enemyTeam = teamPos.getEnemyTeam();
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
  
    private void putResultInGames(List<GameResultDto> gameResultList, List<GameDto> gameLists) {
        TeamDto team1;
        TeamDto team2;
        GameTeamDto teamDto1;
        GameTeamDto teamDto2;
        for (GameDto game : gameLists) {
            team1 = game.getTeam1();
            team2 = game.getTeam2();

            teamDto1 = getTeamDtoFromGamePlayers(team1, game);
            teamDto2 = getTeamDtoFromGamePlayers(team2, game);

            gameResultList.add(GameResultDto.builder()
                    .gameId(game.getId())
                    .team1(teamDto1)
                    .team2(teamDto2)
                    .status(game.getStatus())
                    .time(game.getTime())
                    .build());
        }
    }

    private GameTeamDto getTeamDtoFromGamePlayers(TeamDto team, GameDto game) {
        GameTeamDto teamDto;
        List<GamePlayerDto> gamePlayerList;
        gamePlayerList = new ArrayList<>();
        putGamePlayerDto(game, team.getUser1(), gamePlayerList);
        putGamePlayerDto(game, team.getUser2(), gamePlayerList);
        teamDto = GameTeamDto.builder()
                .isWin(team.getWin())
                .score(team.getScore())
                .players(gamePlayerList)
                .build();
        return teamDto;
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
}
