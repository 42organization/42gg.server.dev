package io.gg.arcade.domain.user.dto;

import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UserDto {
    Integer id;
    String intraId;
    String userImgUri;
    String racketType;
    String statusMessage;
    Boolean isPlaying;
    Integer ppp;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getId())
                .userImgUri(user.getUserImgUri())
                .intraId(user.getIntraId())
                .isPlaying(user.getIsPlaying())
                .racketType(user.getRacketType())
                .ppp(user.getPpp())
                .statusMessage(user.getStatusMessage())
                .build();
    }
}
