package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchSaveGameDto {
    private GameDto game;

    @Override
    public String toString() {
        return "CurrentMatchSaveGameDto{" +
                "game=" + game.toString() +
                '}';
    }
}
