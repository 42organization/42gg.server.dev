package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiAddDto {
    private UserDto user;
    private SlotDto slot;
    private String notiType;
    private String message;
}
