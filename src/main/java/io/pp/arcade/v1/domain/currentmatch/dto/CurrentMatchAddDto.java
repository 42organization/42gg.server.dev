package io.pp.arcade.v1.domain.currentmatch.dto;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchAddDto {
    private UserDto user;
    private GameDto game;
    private SlotDto slot;

    @Override
    public String toString() {
        return "CurrentMatchAddDto{" +
                "user=" + user.toString() +
                ", game=" + game +
                ", slot=" + slot +
                '}';
    }
}
