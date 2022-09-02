package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameModifyStatusDto {
    private Integer gameId;
    private StatusType status;

    @Override
    public String toString() {
        return "GameModifyStatusDto{" +
                "gameId=" + gameId +
                ", status=" + status +
                '}';
    }
}
