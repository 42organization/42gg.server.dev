package io.pp.arcade.domain.game.dto;

import lombok.Getter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class GameResultRequestDto {
    @Min(0)
    @Max(2)
    @NotNull
    private Integer myTeamScore;

    @Min(0)
    @Max(2)
    @NotNull
    private Integer enemyTeamScore;
}
