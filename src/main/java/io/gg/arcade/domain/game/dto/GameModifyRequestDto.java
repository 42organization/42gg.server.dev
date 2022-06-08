package io.gg.arcade.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameModifyRequestDto {
    private Integer gameId;
    private Integer team1Score;
    private Integer team2Score;
}
