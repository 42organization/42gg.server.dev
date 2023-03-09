package io.pp.arcade.v1.admin.season.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
public class SeasonCreateRequestDto {

    @NotNull
    private String seasonName;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime startTime;
    @NotNull
    private Integer startPpp;
    @NotNull
    private Integer pppGap;
    @NotNull
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
