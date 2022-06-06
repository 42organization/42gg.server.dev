package io.gg.arcade.domain.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameInfoDto {
    private String team1Id;
    private String team2Id;
    private Integer team1Score;
    private Integer team2Score;
    private Integer team1Ppp;
    private Integer team2Ppp;
}
