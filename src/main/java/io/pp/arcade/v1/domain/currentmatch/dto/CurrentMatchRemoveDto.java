package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchRemoveDto {
    private UserDto user;
    private SlotDto slot;
    private GameDto game;

    @Override
    public String toString() {
        return "CurrentMatchRemoveDto{" +
                "user=" + user.toString() +
                ", slot=" + slot.toString() +
                ", game=" + game +
                '}';
    }
}
