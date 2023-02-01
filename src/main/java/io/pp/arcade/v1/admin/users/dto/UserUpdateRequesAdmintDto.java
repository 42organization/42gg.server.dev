package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.global.type.RacketType;
import lombok.Getter;

@Getter
public class UserUpdateRequesAdmintDto {
    private Integer userId;
    private String userImageUri;
    private RacketType racketType;
    private String statusMessage;
    private Integer ppp;
}
