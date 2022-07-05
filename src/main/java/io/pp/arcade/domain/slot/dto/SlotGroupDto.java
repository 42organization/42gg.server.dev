package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class SlotGroupDto {
    List<SlotStatusDto> slots;
}
