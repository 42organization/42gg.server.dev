package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchFindByGameDto {
    private GameDto game;
}
