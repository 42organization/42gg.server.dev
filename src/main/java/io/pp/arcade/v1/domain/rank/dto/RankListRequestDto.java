package io.pp.arcade.v1.domain.rank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankListRequestDto {
    private Integer count;
    private Integer seasonId;

    @Override
    public String toString() {
        return "RankListRequestDto{" +
                "count=" + count +
                ", season=" + seasonId +
                '}';
    }
}
