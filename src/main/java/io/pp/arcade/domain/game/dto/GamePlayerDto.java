package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GamePlayerDto {
    private String intraId;
    private String userImageUri;
    private Integer wins;
    private Integer losses;
    private Double winRate;
    private Integer pppChange;
}
