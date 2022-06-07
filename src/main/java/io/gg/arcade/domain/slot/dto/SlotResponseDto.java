package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Getter
@Builder
public class SlotResponseDto {
    private Integer slotId;
    private String status;
    private Integer headCount;
}
