package io.pp.arcade.v1.admin.season.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Getter
public class SeasonCreateRequestDto {
    private String seasonName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime startTime;
    private Integer startPpp;
    private Integer pppGap;
    private Mode seasonMode;

    @Override
    public String toString() {
        return "SeasonCreateRequestAdminDto{" + '\'' +
                "seasonName=" + seasonName + '\'' +
                ", startTime=" + startTime +
                ", startPpp='" + startPpp + '\'' +
                ", pppGap='" + pppGap + '\'' +
                ", seasonMode=" + seasonMode +
                '}';
    }
}
