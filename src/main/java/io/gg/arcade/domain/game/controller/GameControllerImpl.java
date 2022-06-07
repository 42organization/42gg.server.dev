package io.gg.arcade.domain.game.controller;

import io.gg.arcade.domain.game.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@AllArgsConstructor
public class GameControllerImpl implements GameController{
    private final GameService gameService;

    @GetMapping(value = "/games/{gameId}/result")
    public void GameResult(@PathVariable Integer gameId, HttpServletRequest request){

    }
}
