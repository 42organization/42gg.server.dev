package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RedisRankingFindDto {
    private RankRedis rank;
    private RankKeyGetDto keyGetDto;

    @Builder
    public RedisRankingFindDto(RankRedis rank, RankKeyGetDto keyGetDto) {
        this.rank = rank;
        this.keyGetDto = keyGetDto;
    }
}
