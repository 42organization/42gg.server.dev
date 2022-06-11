package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchDto {
    private Integer userId;
    private SlotDto slot;
    private Integer gameId;
    // slotType
    private Boolean isMatched;
}
