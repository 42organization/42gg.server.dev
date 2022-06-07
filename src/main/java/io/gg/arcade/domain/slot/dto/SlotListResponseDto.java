package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlotListResponseDto {
    private List<SlotResponseDto> slotDtoList;
    private String myMatchId;
}
