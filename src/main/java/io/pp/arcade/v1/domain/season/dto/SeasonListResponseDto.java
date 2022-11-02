package io.pp.arcade.v1.domain.season.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeasonListResponseDto {
    String seasonMode;
    List<SeasonNameDto> seasonList;
}
