package io.pp.arcade.domain.game.Manager.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class GamePlayer {
    protected String intraId;
    protected String userImageUri;
}
