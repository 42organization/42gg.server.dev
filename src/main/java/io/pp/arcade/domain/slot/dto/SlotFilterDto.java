package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotFilterDto {
    private Integer slotId;
    private Integer userSlotId;
    private LocalDateTime slotTime;
    private GameType slotType;
    private GameType gameType;
    private Integer userPpp;
    private Integer gamePpp;
    private Integer headCount;
    private Integer pppGap;
}
