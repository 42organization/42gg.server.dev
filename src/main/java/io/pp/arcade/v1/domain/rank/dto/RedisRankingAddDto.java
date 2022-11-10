package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankingAddDto {
    private RankRedis rank;
    private String rankingKey;

    @Builder
    public RedisRankingAddDto(RankRedis rank, String rankingKey) {
        this.rank = rank;
        this.rankingKey = rankingKey;
    }
}
