package io.gg.arcade.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String intraId;
    private String userImageUri;
    private String racketType;
    private String statusMessage;
}
