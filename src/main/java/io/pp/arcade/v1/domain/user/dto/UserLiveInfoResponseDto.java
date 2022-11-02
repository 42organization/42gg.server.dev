package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLiveInfoResponseDto { 
    Integer notiCount;
    String event;
    String currentMatchMode;

    @Override
    public String toString() {
        return "UserLiveInfoResponseDto{" +
                "notiCount=" + notiCount +
                ", event='" + event + '\'' +
                ", currentMatchMode=" + currentMatchMode +
                '}';
    }
}