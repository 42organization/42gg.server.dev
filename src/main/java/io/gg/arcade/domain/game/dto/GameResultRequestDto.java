package io.gg.arcade.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResultRequestDto {
    private Integer gameId;
    private Integer myTeamScore;
    private Integer enemyTeamScore;
}
