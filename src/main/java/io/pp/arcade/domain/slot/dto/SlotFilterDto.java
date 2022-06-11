package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotFilterDto {
    private Integer slotId;
    private Integer userSlotId;
    private LocalDateTime slotTime;
    private String slotType;
    private String requestType;
    private Integer userPpp;
    private Integer gamePpp;
    private Integer headCount;
}
