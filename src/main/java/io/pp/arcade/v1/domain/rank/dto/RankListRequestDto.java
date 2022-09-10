package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Getter;

@Getter
public class RankListRequestDto {
    private GameType type;

    @Override
    public String toString() {
        return "RankListRequestDto{" +
                "type=" + type +
                '}';
    }
}
