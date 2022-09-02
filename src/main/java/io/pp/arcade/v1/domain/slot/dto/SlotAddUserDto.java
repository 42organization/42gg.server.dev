package io.pp.arcade.v1.domain.slot.dto;

import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotAddUserDto {
    private Integer slotId;
    private GameType type;
    private Integer joinUserPpp;
    private Mode mode;

    @Override
    public String toString() {
        return "SlotAddUserDto{" +
                "slotId=" + slotId +
                ", type=" + type +
                ", joinUserPpp=" + joinUserPpp +
                ", mode=" + mode +
                '}';
    }
}
