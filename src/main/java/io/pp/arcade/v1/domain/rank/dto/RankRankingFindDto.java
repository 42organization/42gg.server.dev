package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RankRankingFindDto {
    Integer userId;
    SeasonDto seasonDto;

    @Builder
    public RankRankingFindDto(Integer userId, SeasonDto seasonDto) {
        this.userId = userId;
        this.seasonDto = seasonDto;
    }
}
