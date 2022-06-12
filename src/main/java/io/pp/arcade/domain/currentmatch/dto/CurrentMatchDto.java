package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchDto {
    private Integer userId;
    private SlotDto slot;
    private Boolean matchImminent;
    private GameDto game;
    // slotType
    private Boolean isMatched;
}
