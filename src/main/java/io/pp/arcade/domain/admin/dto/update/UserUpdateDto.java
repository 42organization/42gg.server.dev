package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.type.RacketType;
import lombok.Getter;

@Getter
public class UserUpdateDto {
    String email;
    String userImageUri;
    RacketType racketType;
    String statusMessage;
    Integer ppp;
}
