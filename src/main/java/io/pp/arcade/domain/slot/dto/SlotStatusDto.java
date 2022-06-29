package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
public class SlotStatusDto {
    private Integer slotId;
    private LocalDateTime time;
    private String status;
    private Integer headCount;

    @Builder
    public SlotStatusDto(Integer slotId, LocalDateTime time, SlotStatusType status, Integer headCount) {
        this.slotId = slotId;
        this.time = time;
        this.status = status.getCode().toLowerCase(Locale.ROOT);
        this.headCount = headCount;
    }
}
