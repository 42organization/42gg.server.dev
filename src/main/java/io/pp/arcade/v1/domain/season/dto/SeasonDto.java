package io.pp.arcade.v1.domain.season.dto;

import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SeasonDto {
    private Integer id;
    private String seasonName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer startPpp;
    private Integer pppGap;
    private Mode seasonMode;

    public static SeasonDto from(Season season) {
        return SeasonDto.builder()
                .id(season.getId())
                .seasonName(season.getSeasonName())
                .startTime(season.getStartTime())
                .endTime(season.getEndTime())
                .startPpp(season.getStartPpp())
                .pppGap(season.getPppGap())
                .seasonMode(season.getSeasonMode())
                .build();
    }

    @Override
    public String toString() {
        return "SeasonDto{" +
                "id=" + id +
                ", seasonName='" + seasonName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startPpp=" + startPpp +
                ", pppGap=" + pppGap +
                ", seasonMode=" + seasonMode +
                '}';
    }
}
