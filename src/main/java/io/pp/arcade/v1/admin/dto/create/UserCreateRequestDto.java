package io.pp.arcade.v1.admin.dto.create;

import io.pp.arcade.v1.global.type.RacketType;
import lombok.Getter;

@Getter
public class UserCreateRequestDto {
    String intraId;
    String email;
    String userImageUri;
    RacketType racketType;
    String statusMessage;
    Integer ppp;
}
