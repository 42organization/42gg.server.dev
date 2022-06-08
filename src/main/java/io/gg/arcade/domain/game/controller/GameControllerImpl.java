package io.gg.arcade.domain.game.controller;

import io.gg.arcade.common.utils.EloRating;
import io.gg.arcade.domain.game.dto.GameDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;
import io.gg.arcade.domain.game.dto.GameResultRequestDto;
import io.gg.arcade.domain.game.service.GameService;
import io.gg.arcade.domain.pchange.dto.PchangeAddRequestDto;
import io.gg.arcade.domain.pchange.service.PchangeService;
import io.gg.arcade.domain.team.dto.TeamDto;
import io.gg.arcade.domain.team.service.TeamService;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;
import io.gg.arcade.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/pingpong")
@AllArgsConstructor
public class GameControllerImpl implements GameController{
    private final GameService gameService;
    private final UserService userService;
    private final TeamService teamService;
    private final PchangeService pchangeService;

    @Override
    @PostMapping(value = "/games/{gameId}/result")
    public void saveGameResult(Integer gameId, GameResultRequestDto gameDto, Integer userId, HttpServletRequest request){
        // 스코어 크기가 2이상인지 확인
        UserDto user = userService.findById(userId);
        GameDto game = gameService.findById(gameDto.getGameId());
        TeamDto team1 = teamService.findTeamByTeamId(game.getTeam1Id());
        TeamDto team2 = teamService.findTeamByTeamId(game.getTeam2Id());
        TeamDto currentUserTeam = null;
        TeamDto enemyTeam = null;
        Integer team1Score;
        Integer team2Score;
        Boolean isWin;

        if (team1.getUser1().getId().equals(user.getId())
                || team1.getUser2().getId().equals(user.getId())) {
            currentUserTeam = team1;
            enemyTeam = team2;
            team1Score = gameDto.getMyTeamScore();
            team2Score = gameDto.getEnemyTeamScore();
        } else if (team2.getUser1().getId().equals(user.getId())
                || team2.getUser2().getId().equals(user.getId())) {
            currentUserTeam = team2;
            enemyTeam = team1;
            team1Score = gameDto.getEnemyTeamScore();
            team2Score = gameDto.getMyTeamScore();
        } else {
            throw new RuntimeException();
        }

        // 게임 정보 수정 (팀 스코어, 팀 Win)
        GameModifyRequestDto modifyRequestDto = GameModifyRequestDto.builder()
                                                .gameId(game.getId())
                                                .team1Score(team1Score)
                                                .team2Score(team2Score)
                                                .build();
        GameDto modifyGameDto = gameService.modifyGame(modifyRequestDto);

        if (gameDto.getMyTeamScore().equals(2)) {
            isWin = true;
        } else {
            isWin = false;
        }

        // 매칭 모든 유저 PPP 수정
        Integer pppChange;

        if (currentUserTeam.getUser1() != null) {
            pppChange = EloRating.pppChange(currentUserTeam.getUser1().getPpp(), enemyTeam.getTeamPpp(), isWin);
            UserModifyPppRequestDto userModifyPppRequestDto1 = UserModifyPppRequestDto.builder()
                    .userId(currentUserTeam.getUser1().getId())
                    .ppp(pppChange)
                    .build();
            userService.UserModifyPpp(userModifyPppRequestDto1);

            PchangeAddRequestDto pchangeAddRequestDto = PchangeAddRequestDto.builder()
                    .gameId(game.getId())
                    .userId(currentUserTeam.getUser1().getId())
                    .pppChange(pppChange)
                    .pppResult(currentUserTeam.getUser1().getPpp() + pppChange)
                    .build();
            pchangeService.addPchange(pchangeAddRequestDto);

        }
        if (currentUserTeam.getUser2() != null) {
            pppChange = EloRating.pppChange(currentUserTeam.getUser2().getPpp(), enemyTeam.getTeamPpp(), isWin);
            UserModifyPppRequestDto userModifyPppRequestDto2 = UserModifyPppRequestDto.builder()
                    .userId(currentUserTeam.getUser2().getId())
                    .ppp(pppChange)
                    .build();
            userService.UserModifyPpp(userModifyPppRequestDto2);

            PchangeAddRequestDto pchangeAddRequestDto = PchangeAddRequestDto.builder()
                    .gameId(game.getId())
                    .userId(currentUserTeam.getUser2().getId())
                    .pppChange(pppChange)
                    .pppResult(currentUserTeam.getUser2().getPpp() + pppChange)
                    .build();
            pchangeService.addPchange(pchangeAddRequestDto);
        }
        if (enemyTeam.getUser1() != null) {
            pppChange = EloRating.pppChange(enemyTeam.getUser1().getPpp(), currentUserTeam.getTeamPpp(), !isWin);
            UserModifyPppRequestDto userModifyPppRequestDto = UserModifyPppRequestDto.builder()
                    .userId(enemyTeam.getUser1().getId())
                    .ppp(pppChange)
                    .build();
            userService.UserModifyPpp(userModifyPppRequestDto);

            PchangeAddRequestDto pchangeAddRequestDto = PchangeAddRequestDto.builder()
                    .gameId(game.getId())
                    .userId(enemyTeam.getUser1().getId())
                    .pppChange(pppChange)
                    .pppResult(enemyTeam.getUser1().getPpp() + pppChange)
                    .build();
            pchangeService.addPchange(pchangeAddRequestDto);
        }
        if (enemyTeam.getUser2() != null) {
            pppChange = EloRating.pppChange(enemyTeam.getUser2().getPpp(), currentUserTeam.getTeamPpp(), !isWin);
            UserModifyPppRequestDto userModifyPppRequestDto4 = UserModifyPppRequestDto.builder()
                    .userId(enemyTeam.getUser2().getId())
                    .ppp(pppChange)
                    .build();
            userService.UserModifyPpp(userModifyPppRequestDto4);

            PchangeAddRequestDto pchangeAddRequestDto = PchangeAddRequestDto.builder()
                    .gameId(game.getId())
                    .userId(enemyTeam.getUser2().getId())
                    .pppChange(pppChange)
                    .pppResult(enemyTeam.getUser2().getPpp() + pppChange)
                    .build();
            pchangeService.addPchange(pchangeAddRequestDto);
        }

        // 랭킹 PPP 수정 (레디스 ?)
    }

    @Override
    @GetMapping("/games/{gameId}")
    public GameDto gameResult(Integer gameId) {
        return gameService.findById(gameId);
    }
}
