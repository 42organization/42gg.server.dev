package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUserInfoDto {
    private String intraId;
    private String userImageUri;

    @Override
    public String toString() {
        return "GameUserInfoDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                '}';
    }

    public static GameUserInfoDto from(User user) {
        return GameUserInfoDto.builder()
                .intraId(user.getIntraId())
                .userImageUri(user.getImageUri())
                .build();
    }
}
