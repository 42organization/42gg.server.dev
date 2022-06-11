package io.pp.arcade.domain.user.dto;

import io.pp.arcade.domain.slot.dto.SlotStatusDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlotFindResponseDto {
    List<SlotStatusDto> matchBoards;
}
