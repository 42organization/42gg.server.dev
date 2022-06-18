package io.pp.arcade.domain.admin.dto.create;

import io.pp.arcade.global.type.RacketType;
import lombok.Getter;

@Getter
public class UserCreateDto {
    String intraId;
    String eMail;
    String userImageUri;
    RacketType racketType;
    String statusMessage;
    Integer ppp;
}
