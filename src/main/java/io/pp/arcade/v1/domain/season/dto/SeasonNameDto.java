package io.pp.arcade.v1.domain.season.dto;

import io.pp.arcade.v1.domain.season.Season;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeasonNameDto {
    Integer id;
    String name;

    static SeasonNameDto from(Season season) {
        return SeasonNameDto.builder()
                .id(season.getId())
                .name(season.getSeasonName())
                .build();
    }
}
