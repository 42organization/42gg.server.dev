package io.pp.arcade.v1.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankingFindListDto {
    private Integer start;
    private Integer end;
    private String curRankingKey;

    private RankKeyGetDto rankKeyGetDto;

    @Builder
    public RedisRankingFindListDto(Integer start, Integer end, String curRankingKey, RankKeyGetDto rankKeyGetDto) {
        this.start = start;
        this.end = end;
        this.curRankingKey = curRankingKey;
        this.rankKeyGetDto = rankKeyGetDto;
    }
}
