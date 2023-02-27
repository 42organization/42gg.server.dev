package io.pp.arcade.v1.admin.slot.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SlotPolicy {
    int interval;
    int futureTimeGap;
    int pastTimeGap;
}
