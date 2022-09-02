package io.pp.arcade.v1.domain.season.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeasonNameDto {
    Integer id;
    String name;
}
