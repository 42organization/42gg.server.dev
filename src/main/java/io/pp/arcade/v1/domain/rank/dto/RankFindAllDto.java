package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankFindAllDto {
    GameType gameType;

    @Override
    public String toString() {
        return "RankFindAllDto{" +
                "gameType=" + gameType +
                '}';
    }
}
