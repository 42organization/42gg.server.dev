package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotStatusDto {
    private Integer slotId;
    private LocalDateTime time;
    private String status;
    private Integer headCount;
}
