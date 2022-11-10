package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RankSaveAllDto {
    private List<RankUserDto> rankUserDtos;
    private SeasonDto seasonDto;

    @Override
    public String toString() {
        return "RankSaveAllDto{" +
                "rankUserDtos=" + rankUserDtos +
                ", seasonDto=" + seasonDto +
                '}';
    }
}
