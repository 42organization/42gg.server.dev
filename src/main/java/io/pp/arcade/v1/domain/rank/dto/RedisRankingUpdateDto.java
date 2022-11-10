package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankingUpdateDto {
    private String rankingKey;
    private RankRedis rank;
    private Integer ppp;

    @Builder
    public RedisRankingUpdateDto(String rankingKey, RankRedis rank, Integer ppp) {
        this.rankingKey = rankingKey;
        this.rank = rank;
        this.ppp = ppp;
    }
}
