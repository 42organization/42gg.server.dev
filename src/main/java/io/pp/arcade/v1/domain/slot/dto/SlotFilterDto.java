package io.pp.arcade.v1.domain.slot.dto;

import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotFilterDto {
    private SlotDto slot;
    private Integer userSlotId;
    private Integer userPpp;
    private GameType gameType;
    private Integer pppGap;
    private Mode userMode;

    @Override
    public String toString() {
        return "SlotFilterDto{" +
                "slot=" + slot.toString() +
                ", userSlotId=" + userSlotId +
                ", gameType=" + gameType +
                ", userPpp=" + userPpp +
                ", pppGap=" + pppGap +
                ", mode=" + userMode +
                '}';
    }
}
