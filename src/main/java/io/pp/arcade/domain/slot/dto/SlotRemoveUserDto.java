package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotRemoveUserRequestDto {
    private Integer slotId;
    private Integer exitUserPpp;
}
