package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.util.RacketType;
import lombok.Getter;

@Getter
public class UserUpdateDto {
    String userImageUri;
    RacketType racketType;
    String statusMessage;
    Integer ppp;
}
