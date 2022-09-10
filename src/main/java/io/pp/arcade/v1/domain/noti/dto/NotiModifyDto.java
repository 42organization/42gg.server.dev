package io.pp.arcade.v1.domain.noti.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiModifyDto {
    private UserDto user;

    @Override
    public String toString() {
        return "NotiModifyDto{" +
                "user=" + user +
                '}';
    }
}
