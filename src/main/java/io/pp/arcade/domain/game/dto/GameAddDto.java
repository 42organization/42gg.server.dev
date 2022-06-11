package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameAddRequestDto {
    private SlotDto slotDto;
}
