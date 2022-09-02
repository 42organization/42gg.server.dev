package io.pp.arcade.v1.domain.game.Manager.data;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GamePlayerNormal extends GamePlayer {
    private Integer level;

    @Builder
    public GamePlayerNormal(String intraId, String userImageUri, Integer level) {
        super(intraId, userImageUri);
        this.level = level;
    }

    @Override
    public String toString() {
        return "GamePlayerNormalDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", level=" + level +
                '}';
    }
}
