package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SlotAddUserDto {
    private Integer slotId;
    private GameType type;
    private Integer joinUserPpp;
}
