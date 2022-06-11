package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.game.dto.GameResultRequestDto;
import io.pp.arcade.domain.game.dto.GameResultResponseDto;
import io.pp.arcade.domain.game.dto.GameUserInfoResponseDto;
import io.pp.arcade.domain.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class GameControllerImpl implements GameController{
    private final GameService gameService;
    private final TeamService teamService;


    @Override
    @GetMapping(value = "/games/{gameId}/result")
    public GameDto gameUserInfo(@PathVariable Integer gameId) {
        return GameService.findById(gameId);

    }

    @Override
    @PostMapping(value = "/games/{gameId}/result")
    public void gameResultGetter(Integer gameId, GameResultRequestDto requestDto) {

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
