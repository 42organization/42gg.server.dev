package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.util.RacketType;
import lombok.Getter;

@Getter
public class UserModifyProfileRequestDto {
    private RacketType racketType;
    private String statusMessage;
}
