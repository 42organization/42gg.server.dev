package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankAddDto {
    private String key;
    private Integer userId;
    private RankRedis rank;

    @Builder
    public RedisRankAddDto(String key, Integer userId, RankRedis rank) {
        this.key = key;
        this.userId = userId;
        this.rank = rank;
    }
}
