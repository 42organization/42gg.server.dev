package io.pp.arcade.domain.season.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SeasonFindDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
