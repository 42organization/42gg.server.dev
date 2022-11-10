package io.pp.arcade.v1.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RankKeyGetDto {
    Integer seasonId;
    String seasonName;

    @Builder
    public RankKeyGetDto(Integer seasonId, String seasonName) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }
}
