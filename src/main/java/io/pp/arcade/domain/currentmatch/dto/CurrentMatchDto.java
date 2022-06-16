package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.currentmatch.CurrentMatch;
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

    public static CurrentMatchDto from(CurrentMatch currentMatch) {
        return CurrentMatchDto.builder()
                .userId(currentMatch.getUser().getId())
                .slot(SlotDto.from(currentMatch.getSlot()))
                .game(GameDto.from(currentMatch.getGame()))
                .matchImminent(currentMatch.getMatchImminent())
                .isMatched(currentMatch.getIsMatched())
                .build();
    }
}
