package io.pp.arcade.domain.admin.dto.create;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameCreateRequestDto {
    private Integer slotId;
    private Integer seasonId;
    private String status;
}
