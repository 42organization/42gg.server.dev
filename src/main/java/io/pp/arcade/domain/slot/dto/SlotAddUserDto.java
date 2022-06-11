package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotAddUserDto {
    private Integer slotId;
    private String type;
    private Integer joinUserPpp;
}
