package io.pp.arcade.v1.domain.slot.dto;

import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlotStatusDto {
    private Integer slotId;
    private LocalDateTime time;
    private LocalDateTime endTime;
    private String status;
    private Integer headCount;
    private String mode;

    @Builder
    public SlotStatusDto(Integer slotId, LocalDateTime time, LocalDateTime endTime, SlotStatusType status, Integer headCount, Mode mode) {
        this.slotId = slotId;
        this.time = time;
        this.endTime = endTime;
        this.status = status.getCode();
        this.headCount = headCount;
        this.mode = mode.getCode();
    }

    @Override
    public String toString() {
        return "SlotStatusDto{" +
                "slotId=" + slotId +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", headCount=" + headCount +
                ", mode=" + mode +
                '}';
    }
}
