package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotRemoveUserDto {
    private Integer slotId;
    private Integer exitUserPpp;
}
