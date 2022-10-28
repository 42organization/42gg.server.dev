package io.pp.arcade.v1.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameResultResponseDto {
    List<GameResultDto> games;
    Integer lastGameId;
    Boolean isLast;

    @Override
    public String toString() {
        return "GameResultResponseDto{" +
                "games=" + games +
                ", lastGameId=" + lastGameId +
                ", isLast=" + isLast +
                '}';
    }
}
