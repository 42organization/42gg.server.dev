package io.pp.arcade.domain.game.dto;

import io.pp.arcade.global.exception.BusinessException;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
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

    public Integer getMyTeamScore() {
        if (myTeamScore + enemyTeamScore > 3) {
            throw new BusinessException("E0001");
        } else if (myTeamScore == 1 && enemyTeamScore == 1) {
            throw new BusinessException("E0001");
        }
        return myTeamScore;
    }

    public Integer getEnemyTeamScore() {
        if (myTeamScore + enemyTeamScore > 3) {
            throw new BusinessException("E0001");
        } else if (myTeamScore == 1 && enemyTeamScore == 1) {
            throw new BusinessException("E0001");
        }
        return enemyTeamScore;
    }
}
