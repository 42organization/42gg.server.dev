package io.pp.arcade.v1.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankingFindListDto {
    private Integer start;
    private Integer end;
    private String rankingKey;

    private RankKeyGetDto rankKeyGetDto;

    @Builder
    public RedisRankingFindListDto(Integer start, Integer end, String rankingKey, RankKeyGetDto rankKeyGetDto) {
        this.start = start;
        this.end = end;
        this.rankingKey = rankingKey;
        this.rankKeyGetDto = rankKeyGetDto;
    }
}
