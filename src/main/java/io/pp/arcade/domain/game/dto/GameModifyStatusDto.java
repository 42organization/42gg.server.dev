package io.pp.arcade.domain.game.dto;

import io.pp.arcade.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GameModifyStatusDto {
    private Integer gameId;
    private StatusType status;
}
