package io.pp.arcade.v1.domain.season.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeasonDeleteDto {
    private Integer seasonId;

    @Override
    public String toString() {
        return "SeasonDeleteDto{" +
                "seasonId=" + seasonId +
                '}';
    }
}
