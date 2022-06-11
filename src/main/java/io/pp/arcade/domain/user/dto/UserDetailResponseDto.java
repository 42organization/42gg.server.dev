package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailResponseDto {
    private String userId;
    private String userImageUri;
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private Integer winRate;
    private RacketType racketType;
    private String statusMessage;
}
