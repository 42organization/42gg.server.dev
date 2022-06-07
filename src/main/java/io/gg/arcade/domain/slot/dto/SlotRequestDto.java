package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SlotRequestDto {
    private Integer slotId;
    private Integer gamePpp;
    private String type;

    @Builder
    public SlotRequestDto(Integer slotId, Integer gamePpp, String type) {
        this.slotId = slotId;
        this.gamePpp = gamePpp;
        this.type = type;
    }
}
