package io.pp.arcade.v1.domain.game.Manager.data;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GamePlayerRank extends GamePlayer {
    private Integer wins;
    private Integer losses;
    private Double winRate;
    private Integer pppChange;

    @Builder
    public GamePlayerRank(String intraId, String userImageUri, Integer wins, Integer losses, Double winRate, Integer pppChange) {
        super(intraId, userImageUri);
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
        this.pppChange = pppChange;
    }

    @Override
    public String toString() {
        return "GamePlayerRankDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", wins=" + wins +
                ", losses=" + losses +
                ", winRate=" + winRate +
                ", pppChange=" + pppChange +
                '}';
    }
}
