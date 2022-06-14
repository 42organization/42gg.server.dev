package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotiAddDto {
    private UserDto userDto;
    private SlotDto slotDto;
    private String notiType;
    private String message;
}
