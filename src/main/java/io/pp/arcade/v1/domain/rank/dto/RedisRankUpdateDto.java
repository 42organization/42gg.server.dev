package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankUpdateDto {
    private String seasonKey;
    private Integer userId;
    private RankRedis userRank;

    @Builder
    public RedisRankUpdateDto(String seasonKey, Integer userId, RankRedis userRank) {
        this.seasonKey = seasonKey;
        this.userId = userId;
        this.userRank = userRank;
    }
}
