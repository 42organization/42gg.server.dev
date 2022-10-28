package io.pp.arcade.v1.domain.slot.dto;

import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlotStatusDto {
    private SlotDto slot;
    private String status;

    @Builder
    public SlotStatusDto(SlotDto slot, SlotStatusType status) {
        this.slot = slot;
        this.status = status.getCode();
    }

    @Override
    public String toString() {
        return "SlotStatusDto{" +
                "slot=" + slot.toString() +
                ", status='" + status + '\'' +
                '}';
    }
}
