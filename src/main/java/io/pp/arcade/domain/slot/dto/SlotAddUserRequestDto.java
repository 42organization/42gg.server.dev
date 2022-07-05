package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@ToString
public class SlotAddUserRequestDto {
    @NotNull
    @Positive
    private Integer slotId;
}
