package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotStatusDto {
    private Integer slotId;
    private LocalDateTime time;
    private SlotStatusType status;
    private Integer headCount;
}
