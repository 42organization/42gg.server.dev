package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamModifyGameResultDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.global.util.EloRating;
import lombok.AllArgsConstructor;
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
        List<GameUserInfoDto> myTeams = new ArrayList<>();
        List<GameUserInfoDto> enemyTeams = new ArrayList<>();
        ;
        TeamDto team1 = game.getTeam1();
        TeamDto team2 = game.getTeam2();
        TeamDto myTeam;
        TeamDto enemyTeam;

        //figuring out team number for myteam and enemyteam
        if (userId.equals(team1.getUser1().getId()) || userId.equals(team1.getUser2().getId())) {
            myTeam = team1;
            enemyTeam = team2;
        } else if (userId.equals(team2.getUser1().getId()) || userId.equals(team2.getUser2().getId())) {
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
            myTeams.add(GameUserInfoDto.builder().userId(myTeamUser1.getId()).userImageUri(myTeamUser1.getImageUri()).build());
        }
        if (myTeamUser2 != null) {
            myTeams.add(GameUserInfoDto.builder().userId(myTeamUser2.getId()).userImageUri(myTeamUser2.getImageUri()).build());
        }
        if (enemyTeamUser1 != null) {
            enemyTeams.add(GameUserInfoDto.builder().userId(enemyTeamUser1.getId()).userImageUri(enemyTeamUser1.getImageUri()).build());
        }
        if (enemyTeamUser2 != null) {
            enemyTeams.add(GameUserInfoDto.builder().userId(enemyTeamUser2.getId()).userImageUri(enemyTeamUser2.getImageUri()).build());
        }
        GameUserInfoResponseDto gameUserInfoResponseDto = GameUserInfoResponseDto.builder()
                .myTeam(myTeams)
                .enemyTeam(enemyTeams)
                .build();
        return gameUserInfoResponseDto;
    }

    @Override
    @PostMapping(value = "/games/{gameId}/result")
    public void saveGameResult(Integer gameId, GameResultRequestDto requestDto, Integer userId) {
        GameDto game = gameService.findById(gameId);
        TeamDto team1 = game.getTeam1();
        TeamDto team2 = game.getTeam2();
        TeamDto myTeam;
        TeamDto enemyTeam;
        Integer team1Score;
        Integer team2Score;

        //figuring out team number for myteam and enemyteam
        UserDto user = userService.findById(userId);
        if (user.equals(team1.getUser1()) || user.equals(team1.getUser2())) {
            myTeam = team1;
            team1Score = requestDto.getMyTeamScore();
            enemyTeam = team2;
            team2Score = requestDto.getEnemyTeamScore();
        } else if (user.equals(team2.getUser1()) || user.equals(team2.getUser2())) {
            myTeam = team2;
            team2Score = requestDto.getMyTeamScore();
            enemyTeam = team1;
            team1Score = requestDto.getEnemyTeamScore();
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다");
        }

        //modify team with game result
        TeamModifyGameResultDto modifyGameResultDto1 = TeamModifyGameResultDto.builder()
                .teamId(team1.getId())
                .score(team1Score)
                .win(team1Score > team2Score)
                .build();
        TeamModifyGameResultDto modifyGameResultDto2 = TeamModifyGameResultDto.builder()
                .teamId(team2.getId())
                .score(team2Score)
                .win(team2Score > team1Score)
                .build();
        teamService.modifyGameResultInTeam(modifyGameResultDto1);
        teamService.modifyGameResultInTeam(modifyGameResultDto2);

        //modify users with game result
        modifyUserPppAndAddPchange(gameId, myTeam.getUser1(), enemyTeam);
        modifyUserPppAndAddPchange(gameId, myTeam.getUser2(), enemyTeam);
        modifyUserPppAndAddPchange(gameId, enemyTeam.getUser1(), myTeam);
        modifyUserPppAndAddPchange(gameId, enemyTeam.getUser2(), myTeam);

        //modify users' ranks with game result
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

    @Override
    @GetMapping(value = "/games")
    public List<GameResultResponseDto> gameResultByCount(Integer gameId, Integer count) {
        return null;
    }

    @Override
    @GetMapping(value = "/games")
    public List<GameResultResponseDto> gameResultByIndexAndCount(Integer gameId, Integer index, Integer count, String status) {
        return null;
    }

    @Override
    @GetMapping(value = "/games/users/{userId}")
    public List<GameResultResponseDto> gameResultByUserIdAndIndexAndCount(Integer userId, Integer index, Integer count, String type) {
        return null;
    }
}
