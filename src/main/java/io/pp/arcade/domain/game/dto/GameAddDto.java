package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Null;
import java.lang.annotation.Native;

@Getter
@Builder
@ToString
public class GameAddDto {
    private SlotDto slotDto;
}
