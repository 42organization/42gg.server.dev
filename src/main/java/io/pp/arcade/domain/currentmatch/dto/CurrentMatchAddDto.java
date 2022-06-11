package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchAddDto {
    private Integer userId;
    private GameDto game;
    private SlotDto slot;
}
