package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotResponseDto {
    private Integer slotId;
    private String status;
    private Integer headCount;
}
