package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankListRequestDto {
    private Integer count;
    private Integer season;

    @Override
    public String toString() {
        return "RankListRequestDto{" +
                "count=" + count +
                ", season=" + season +
                '}';
    }
}
