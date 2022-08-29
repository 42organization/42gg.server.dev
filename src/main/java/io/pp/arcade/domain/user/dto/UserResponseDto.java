package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String intraId;
    private String userImageUri;
    private Mode mode;
    private Boolean isAdmin;

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", mode=" + mode +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
