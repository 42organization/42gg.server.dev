package io.gg.arcade.domain.game.controller;

import io.gg.arcade.domain.game.dto.GameDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface GameController {
    void saveGameResult(@PathVariable Integer gameId, @RequestBody GameModifyRequestDto gameDto, @RequestParam Integer userId, HttpServletRequest request);
    GameDto gameResult(@PathVariable Integer gameId);
}
