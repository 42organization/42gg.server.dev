package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.util.RacketType;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private Integer userId;
    private String userImageUri;
    private RacketType racketType;
    private String statusMessage;
    private Integer ppp;
}
