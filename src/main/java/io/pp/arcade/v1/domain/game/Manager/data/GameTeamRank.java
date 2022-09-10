package io.pp.arcade.v1.domain.game.Manager.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class GameTeamRank extends GameTeam {
    @Setter
    private Boolean isWin;
    @Setter
    private Integer score;

    @Builder
    public GameTeamRank(List<GamePlayer> players, Boolean isWin, Integer score) {
        super(players);
        this.isWin = isWin;
        this.score = score;
    }

    @Override
    public String toString() {
        return "GameTeamRankDto{" +
                "players=" + players +
                ", isWin=" + isWin +
                ", score=" + score +
                '}';
    }
}
