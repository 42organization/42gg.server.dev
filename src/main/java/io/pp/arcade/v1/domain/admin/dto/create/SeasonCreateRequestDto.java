package io.pp.arcade.v1.domain.admin.dto.create;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SeasonCreateRequestDto {
    private String seasonName;
    private LocalDateTime startTime;
    private Integer startPpp;
    private Integer pppGap;
}