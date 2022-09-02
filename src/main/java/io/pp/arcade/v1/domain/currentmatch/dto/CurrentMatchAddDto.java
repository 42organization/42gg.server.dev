package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchAddDto {
    private Integer userId;
    private GameDto game;
    private SlotDto slot;

    @Override
    public String toString() {
        return "CurrentMatchAddDto{" +
                "userId=" + userId +
                ", game=" + game +
                ", slot=" + slot +
                '}';
    }
}
