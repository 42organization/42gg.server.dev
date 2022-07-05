package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotiModifyDto {
    private UserDto user;
}
