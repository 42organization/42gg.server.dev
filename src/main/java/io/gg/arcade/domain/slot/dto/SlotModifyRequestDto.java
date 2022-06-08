package io.gg.arcade.domain.slot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotModifyRequestDto {
    private Integer slotId;
    private Integer gamePpp;
    private String type;
}
