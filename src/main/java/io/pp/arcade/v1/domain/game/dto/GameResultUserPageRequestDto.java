package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameResultUserPageRequestDto {
    private Integer gameId = Integer.MAX_VALUE;
    private Integer count = 10;
    private Integer season;
    private Mode mode;

    public Integer getGameId() {
        return gameId < 1 ? Integer.MAX_VALUE : gameId;
    }

    public Integer getCount() {
        if (count > 100)
            count = 100;
        else if (count < 1)
            count = 10;
        return count;
    }

    public Integer getSeason() {
        if (season != null && season < 0)
            return 0;
        return season;
    }

    @Override
    public String toString() {
        return "GameResultUserPageRequestDto{" +
                "gameId=" + gameId +
                ", count=" + count +
                ", season=" + season +
                ", mode=" + mode +
                '}';
    }
}
