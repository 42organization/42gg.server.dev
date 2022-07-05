package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserModifyProfileDto {
    private Integer userId;
    private String email;
    private String userImageUri;
    private RacketType racketType;
    private String statusMessage;
}
