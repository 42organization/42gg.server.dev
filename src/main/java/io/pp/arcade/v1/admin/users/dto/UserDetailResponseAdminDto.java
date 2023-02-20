package io.pp.arcade.v1.admin.users.dto;

import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailResponseAdminDto {
    private Integer userId;
    private String intraId;
    private String userImageUri;
    private String racketType;
    private String statusMessage;
    private Integer wins;
    private Integer losses;
    private Integer ppp;
    private String eMail;
    private String roleType;


    @Builder
    public UserDetailResponseAdminDto(Integer userId, String intraId, String userImageUri, Integer level, Integer currentExp, Integer maxExp, RacketType racketType, String statusMessage, Integer wins, Integer losses, Integer ppp, String eMail, String roleType) {
        this.userId = userId;
        this.intraId = intraId;
        this.userImageUri = userImageUri;
        this.racketType = racketType.getCode();
        this.statusMessage = statusMessage;
        this.wins = wins;
        this.losses = losses;
        this.ppp = ppp;
        this.eMail = eMail;
        this.roleType = roleType;
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
                ", eMail='" + eMail + '\'' +
                ", roleType='" + roleType + '\'' +
                '}';
    }
}
