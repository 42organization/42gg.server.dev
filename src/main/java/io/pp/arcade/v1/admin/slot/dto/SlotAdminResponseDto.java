package io.pp.arcade.v1.admin.slot.dto;

import io.pp.arcade.v1.admin.slot.SlotManagement;

public class SlotAdminResponseDto {
    private Integer pastSlotTime;
    private Integer futureSlotTime;
    private Integer interval;
    private Integer openMinute;

    public SlotAdminResponseDto(SlotManagement slotManagement) {
        this.pastSlotTime = slotManagement.getPastSlotTime();
        this.futureSlotTime = slotManagement.getFutureSlotTime();
        this.interval = slotManagement.getInterval();
        this.openMinute = slotManagement.getOpenMinute();
    }
}
