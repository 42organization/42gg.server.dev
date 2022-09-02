package io.pp.arcade.v1.domain.noti.dto;

import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiCanceledTypeDto {
    SlotDto slotDto;
    NotiType notiType;

    @Override
    public String toString() {
        return "NotiCanceledTypeDto{" +
                "slotDto=" + slotDto +
                ", notiType=" + notiType +
                '}';
    }
}
