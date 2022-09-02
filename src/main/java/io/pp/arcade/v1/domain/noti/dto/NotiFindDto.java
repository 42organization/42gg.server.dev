package io.pp.arcade.v1.domain.noti.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiFindDto {
    UserDto user;
    Integer notiId;

    @Override
    public String toString() {
        return "NotiFindDto{" +
                "user=" + user +
                ", notiId=" + notiId +
                '}';
    }
}
