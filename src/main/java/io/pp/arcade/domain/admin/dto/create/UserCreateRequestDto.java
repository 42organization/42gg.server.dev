package io.pp.arcade.domain.admin.dto.create;

import io.pp.arcade.global.util.RacketType;
import lombok.Getter;

@Getter
public class UserCreateRequestDto {
    String intraId;
    String eMail;
    String userImageUri;
    RacketType racketType;
    String statusMessage;
    Integer ppp;
}
