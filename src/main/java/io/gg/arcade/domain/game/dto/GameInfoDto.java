package io.gg.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameInfoDto {
    private String team1Id;
    private String team2Id;
    private Integer team1Score;
    private Integer team2Score;
    private Integer team1Ppp;
    private Integer team2Ppp;

    @Builder
    public GameInfoDto(String team1Id, String team2Id, Integer team1Score, Integer team2Score, Integer team1Ppp, Integer team2Ppp) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.team1Ppp = team1Ppp;
        this.team2Ppp = team2Ppp;
    }
}
