package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModifyProfileDto {
    private Integer userId;
    private String userImageUri;
    private RacketType racketType;
    private String statusMessage;
}
