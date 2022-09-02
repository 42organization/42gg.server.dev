package io.pp.arcade.v1.domain.game.Manager.data;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class GameTeam {
    protected List<GamePlayer> players;
}
