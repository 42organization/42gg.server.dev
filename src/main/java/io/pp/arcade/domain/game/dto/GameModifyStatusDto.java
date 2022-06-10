package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameModifyStatusDto {
    private Integer gameId;
    private String status;
}
