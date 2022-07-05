package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotiCanceledTypeDto {
    SlotDto slotDto;
    NotiType notiType;
}
