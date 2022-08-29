package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLiveInfoResponseDto { 
    Integer notiCount;
    String event;
    Mode currentMatchMode;
    Mode seasonMode;

    @Override
    public String toString() {
        return "UserLiveInfoResponseDto{" +
                "notiCount=" + notiCount +
                ", event='" + event + '\'' +
                ", currentMatchMode=" + currentMatchMode +
                ", seasonMode=" + seasonMode +
                '}';
    }
}