package io.pp.arcade.v1.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameResultListDto {
    List<GameDto> gameList;
    Boolean isLast;
    @Override
    public String toString() {
        return "GameResultListDto{" +
                "gameList=" + gameList +
                ", isLast=" + isLast +
                '}';
    }
}
