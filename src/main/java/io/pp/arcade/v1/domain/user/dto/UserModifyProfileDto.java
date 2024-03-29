package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.SnsType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModifyProfileDto {
    private Integer userId;
    private String email;
    private String userImageUri;
    private RacketType racketType;
    private String statusMessage;
    private SnsType snsNotiOpt;

    @Override
    public String toString() {
        return "UserModifyProfileDto{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", racketType=" + racketType +
                ", statusMessage='" + statusMessage + '\'' +
                ", snsType='" + snsNotiOpt + '\'' +
                '}';
    }
}
