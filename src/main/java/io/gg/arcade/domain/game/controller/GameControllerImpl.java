package io.gg.arcade.domain.game.controller;

import io.gg.arcade.common.utils.EloRating;
import io.gg.arcade.domain.game.dto.GameDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;
import io.gg.arcade.domain.game.service.GameService;
import io.gg.arcade.domain.pchange.dto.PchangeAddRequestDto;
import io.gg.arcade.domain.pchange.service.PchangeService;
import io.gg.arcade.domain.team.dto.TeamDto;
import io.gg.arcade.domain.team.service.TeamService;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;
import io.gg.arcade.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


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
    public void saveGameResult(Integer gameId, GameModifyRequestDto gameDto, Integer userId, HttpServletRequest request){
        // 스코어 크기가 2이상인지 확인

        GameDto game = gameService.findById(gameId);


        List<TeamDto> userDtoList = new ArrayList<>();
        userDtoList.addAll(teamService.findUserListByTeamId(game.getTeam1Id()));
        userDtoList.addAll(teamService.findUserListByTeamId(game.getTeam2Id()));


        // 게임 정보 수정 (팀 스코어, 팀 Win)
        GameModifyRequestDto modifyRequestDto = GameModifyRequestDto.builder()
                                                .gameId(game.getId())
                                                .team1Score(gameDto.getTeam1Score())
                                                .team2Score(gameDto.getTeam2Score())
                                                .build();
        GameDto modifyGameDto = gameService.modifyGame(modifyRequestDto);

        //teamService.findUserListByTeamId();



        // 매칭 모든 유저 PPP 수정
        TeamDto curUserTeam = null;
        TeamDto opponentUserTeam = null;
        for (TeamDto teamDto : userDtoList){
            if (teamDto.getUserDto().getId().equals(userId)) {
                curUserTeam = teamDto;
            } else {
                opponentUserTeam = teamDto;
            }
        }

        Boolean isWin;
        if (curUserTeam.getTeamId().equals(modifyGameDto.getTeam1Id())) {
            isWin = modifyGameDto.getTeam1Win();
        } else {
            isWin = modifyGameDto.getTeam2Win();
        }

        Integer pppChange = EloRating.pppChange(curUserTeam.getUserDto().getPpp(), opponentUserTeam.getUserDto().getPpp(), isWin);
        
        UserModifyPppRequestDto userModifyPppRequestDto1 = UserModifyPppRequestDto.builder()
                .userId(curUserTeam.getUserDto().getId())
                .ppp(pppChange)
                .build();
        userService.UserModifyPpp(userModifyPppRequestDto1);

        UserModifyPppRequestDto userModifyPppRequestDto2 = UserModifyPppRequestDto.builder()
                .userId(opponentUserTeam.getUserDto().getId())
                .ppp(-pppChange)
                .build();
        userService.UserModifyPpp(userModifyPppRequestDto2);
        
        // 게임별 점수변동 추가 (PChange)
        PchangeAddRequestDto pchangeAddRequestDto1 = PchangeAddRequestDto.builder()
                .gameId(game.getId())
                .userId(curUserTeam.getUserDto().getId())
                .pppChange(pppChange)
                .pppResult(curUserTeam.getUserDto().getPpp() + pppChange)
                .build();
        pchangeService.addPchange(pchangeAddRequestDto1);

        PchangeAddRequestDto pchangeAddRequestDto2 = PchangeAddRequestDto.builder()
                .gameId(game.getId())
                .userId(opponentUserTeam.getUserDto().getId())
                .pppChange(-pppChange)
                .pppResult(curUserTeam.getUserDto().getPpp() - pppChange)
                .build();
        pchangeService.addPchange(pchangeAddRequestDto2);
        // 랭킹 PPP 수정 (레디스 ?)
    }

    @Override
    @GetMapping("/games/{gameId}")
    public GameDto gameResult(Integer gameId) {
        return gameService.findById(gameId);
    }
}
