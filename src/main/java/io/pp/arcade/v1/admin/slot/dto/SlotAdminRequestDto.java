package io.pp.arcade.v1.admin.slot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlotAdminRequestDto {
    private Integer pastSlotTime;
    private Integer futureSlotTime;
    private Integer interval;
    private Integer openMinute;
}
