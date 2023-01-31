package io.pp.arcade.v1.admin.dto.update;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Getter;

@Getter
public class SlotUpdateRequestDto {
    private Integer slotId;
    private Integer gamePpp;
    private Integer headCount;
    private GameType type;
}
