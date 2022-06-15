package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlotStatusResponseDto {
    List<SlotGroupDto> slotGroups;
}
