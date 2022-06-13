package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateInfoDto {
    private String userImageUri;
    private String racketType;
    private String statusMessage;
}
