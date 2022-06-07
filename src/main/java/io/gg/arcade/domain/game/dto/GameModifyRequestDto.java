package io.gg.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameModifyRequestDto {
    private Integer gameId;
    private Integer team1Score;
    private Integer team2Score;
}
