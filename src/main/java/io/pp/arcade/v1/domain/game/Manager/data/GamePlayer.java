package io.pp.arcade.v1.domain.game.Manager.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class GamePlayer {
    protected String intraId;
    protected String userImageUri;
}
