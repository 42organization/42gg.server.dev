package io.gg.arcade.domain.slot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotRequestDto {
    private Integer slotId;
    private Integer gamePpp;
    private String type;
}
