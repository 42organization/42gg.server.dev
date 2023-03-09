package io.pp.arcade.v1.admin.season.dto;

import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SeasonAdminDto {

    private Integer seasonId;
    private String seasonName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer startPpp;
    private Integer pppGap;
    private Mode seasonMode;
    private Integer status;

    static public SeasonAdminDto from(Season season) {
        Integer status;
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(season.getEndTime()))
            status = 0; //SEASON_PAST
        else if (now.isAfter(season.getStartTime()) && now.isBefore((season.getEndTime())))
            status = 1; //SEASON_CURRENT
        else
            status = 2; //SEASON_FUTUER
        return SeasonAdminDto.builder()
                .seasonId(season.getId())
                .seasonName(season.getSeasonName())
                .startTime(season.getStartTime())
                .endTime(season.getEndTime())
                .startPpp(season.getStartPpp())
                .pppGap(season.getPppGap())
                .seasonMode(season.getSeasonMode())
                .status(status)
                .build();
    }
}
