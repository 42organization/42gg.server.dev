package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultPageRequestDto {
    private Integer gameId = Integer.MAX_VALUE;
    private Integer count = 10;
    private StatusType status = StatusType.END;
    private Mode mode;
    private Integer season;

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

    public StatusType getStatus() {
        if (status == StatusType.LIVE)
            status = null;
        return status;
    }

    public Integer getSeason() {
        if (season != null && season < 1)
            season = null;
        return season;
    }

    @Override
    public String toString() {
        return "GameResultPageRequestDto{" +
                "gameId=" + gameId +
                ", count=" + count +
                ", status=" + status +
                ", mode=" + mode +
                '}';
    }
}
