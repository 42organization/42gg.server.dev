package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailResponseAdminDto {
    private String intraId;
    private String userImageUri;
    private String racketType;
    private String statusMessage;
    private Integer wins;
    private Integer losses;
    private Integer ppp;
    private String e_mail;
    private String role_type;


    @Builder
    public UserDetailResponseAdminDto(String intraId, String userImageUri, Integer level, Integer currentExp, Integer maxExp, RacketType racketType, String statusMessage, Integer wins, Integer losses, Integer ppp, String e_mail, String role_type) {
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.racketType = racketType.getCode();
        this.statusMessage = statusMessage;
        this.wins = wins;
        this.losses = losses;
        this.ppp = ppp;
        this.e_mail = e_mail;
        this.role_type = role_type;
    }

    @Override
    public String toString() {
        return "UserDetailResponseDto{" +
                "intraId='" + intraId + '\'' +
                ", userImageUri='" + userImageUri + '\'' +
                ", racketType='" + racketType + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", wins='" + wins.toString() + '\'' +
                ", losses='" + losses.toString() + '\'' +
                ", ppp='" + ppp.toString() + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", role_type='" + role_type + '\'' +
                '}';
    }
}
