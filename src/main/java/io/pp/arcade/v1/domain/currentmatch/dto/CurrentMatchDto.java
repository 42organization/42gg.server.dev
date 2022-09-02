package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchDto {
    private Integer id;
    private UserDto user;
    private SlotDto slot;
    private Boolean matchImminent;
    private GameDto game;
    // slotType
    private Boolean isMatched;

    public static CurrentMatchDto from(CurrentMatch currentMatch) {
        if (currentMatch == null) {
            return null;
        }
        return CurrentMatchDto.builder()
                .id(currentMatch.getId())
                .user(UserDto.from(currentMatch.getUser()))
                .slot(SlotDto.from(currentMatch.getSlot()))
                .game(currentMatch.getGame() == null ? null : GameDto.from(currentMatch.getGame()))
                .matchImminent(currentMatch.getMatchImminent())
                .isMatched(currentMatch.getIsMatched())
                .build();
    }

    @Override
    public String toString() {
        return "CurrentMatchDto{" +
                "id=" + id +
                ", user=" + user +
                ", slot=" + slot +
                ", matchImminent=" + matchImminent +
                ", game=" + game +
                ", isMatched=" + isMatched +
                '}';
    }
}
