package io.pp.arcade.v1.admin.season.dto;

import io.pp.arcade.v1.domain.season.dto.SeasonNameDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeasonListAdminResponseDto {
    String seasonMode;
    List<SeasonAdminDto> seasonList;
}
