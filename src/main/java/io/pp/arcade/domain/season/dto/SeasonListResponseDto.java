package io.pp.arcade.domain.season.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeasonListResponseDto {
    List<String> seasonList;
}
