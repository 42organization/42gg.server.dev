package io.pp.arcade.v1.domain.game.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameExpResultResponseDto {
    private Integer currentExp;
    private Integer maxExp;
    private Integer currentLevel;
    private Integer increasedExp;
    private Integer increasedLevel;
}
