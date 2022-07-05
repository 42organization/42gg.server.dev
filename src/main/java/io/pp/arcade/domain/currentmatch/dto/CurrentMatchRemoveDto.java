package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.game.dto.GameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CurrentMatchRemoveDto {
    private Integer userId;
    private GameDto game;
}
