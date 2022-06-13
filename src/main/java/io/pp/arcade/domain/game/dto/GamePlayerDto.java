package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GamePlayerDto {
    private String userId;
    private String userImageUri;
    private Integer wins;
    private Integer losses;
    private Double winRate;
    private Integer pppChange;
}
