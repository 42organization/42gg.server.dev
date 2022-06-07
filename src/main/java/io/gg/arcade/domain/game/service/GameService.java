package io.gg.arcade.domain.game.service;

import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;

public interface GameService {
    void modifyGame(GameModifyRequestDto dto);
    void addGame(GameAddRequestDto dto);
}
