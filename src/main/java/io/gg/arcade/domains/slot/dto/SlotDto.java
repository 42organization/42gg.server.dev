package io.gg.arcade.domains.slot.dto;

import io.gg.arcade.domains.slot.entity.Slot;
import lombok.Getter;

@Getter
public class SlotDto {
    private Integer slotId;
    private Integer headCount;
    private Integer gamePpp;
    private String type;

    public SlotDto(Integer slotId) {
        this.slotId = slotId;
    }

    public Slot toEntity() {
        return Slot.builder()
                .slotId(slotId)
                .gamePpp(gamePpp)
                .headCount(headCount)
                .type(type)
                .build();
    }
}
