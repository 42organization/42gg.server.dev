package io.gg.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSaveRequestDto {
    private String intraId;
    private String userImageUri;
    private String racketType;
    private String statusMessage;

    @Builder
    public UserSaveRequestDto(String intraId, String userImageUri, String racketType, String statusMessage) {
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.racketType = racketType;
        this.statusMessage = statusMessage;
    }
}
