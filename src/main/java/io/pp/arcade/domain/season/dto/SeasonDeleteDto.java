package io.pp.arcade.domain.season.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SeasonDeleteDto {
    private Integer seasonId;
}
