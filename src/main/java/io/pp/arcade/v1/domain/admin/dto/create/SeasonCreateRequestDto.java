package io.pp.arcade.v1.domain.admin.dto.create;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SeasonCreateRequestDto {
    private String seasonName;
    private Integer startPpp;
    private Integer pppGap;
    private Mode seasonMode;
}
