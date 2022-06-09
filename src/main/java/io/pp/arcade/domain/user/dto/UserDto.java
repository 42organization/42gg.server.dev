package io.pp.arcade.domain.user.dto;

import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UserDto {
    private Integer id;
    private String intraId;
    private String imageUri;
    private RacketType racketType;
    private String statusMessage;
    private Integer ppp;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getId())
                .intraId(user.getIntraId())
                .imageUri(user.getImageUri())
                .racketType(user.getRacketType())
                .statusMessage(user.getStatusMessage())
                .ppp(user.getPpp())
                .build();
    }
}
