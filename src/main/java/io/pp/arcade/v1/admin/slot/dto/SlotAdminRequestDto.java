package io.pp.arcade.v1.admin.slot.dto;

import lombok.Getter;

@Getter
public class SlotAdminRequestDto {
    private Integer pastTime;
    private Integer futureTime;
    private Integer interval;
    private Integer revealTime;
}
