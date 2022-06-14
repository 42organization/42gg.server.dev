package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiDeleteDto {
    private UserDto user;
}
