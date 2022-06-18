package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLiveInfoResponseDto { 
    Integer notiCount;
//    Integer gameId;
    String event;
}
