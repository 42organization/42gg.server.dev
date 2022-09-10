package io.pp.arcade.v1.domain.game.Manager.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GameTeamNormal extends GameTeam {
    @Builder
    public GameTeamNormal(List<GamePlayer> players) {
        super(players);
    }
}
