package io.pp.arcade.v1.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlotGroupDto {
    List<SlotStatusDto> slots;

    @Override
    public String toString() {
        return "SlotGroupDto{" +
                "slots=" + slots +
                '}';
    }
}
