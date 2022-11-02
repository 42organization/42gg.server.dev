package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String intraId;
    private String userImageUri;
    private Boolean isAdmin;

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
