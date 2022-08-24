package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GamePlayerNormalDto {
    private String intraId;
    private String userImageUri;
    private Integer level;

    @Override
    public String toString() {
        return "GamePlayerNormal{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", level=" + level +
                '}';
    }
}
