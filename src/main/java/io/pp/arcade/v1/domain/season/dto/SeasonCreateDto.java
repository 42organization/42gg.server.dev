package io.pp.arcade.v1.domain.season.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeasonCreateDto {
    private String seasonName;
    private Integer startPpp;
    private Integer pppGap;
    private Mode seasonMode;

    @Builder
    public SeasonCreateDto(String seasonName, Integer startPpp, Integer pppGap, Mode seasonMode) {
        this.seasonName = seasonName;
        this.startPpp = startPpp;
        this.pppGap = pppGap;
        this.seasonMode = seasonMode;
    }
}
