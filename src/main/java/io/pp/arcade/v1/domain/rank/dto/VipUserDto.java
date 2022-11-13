package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VipUserDto {
    private String intraId;
    private Integer rank;
    private String statusMessage;
    private Integer level;
    private Integer exp;

    public static VipUserDto from (User user, Integer rank){
        VipUserDto dto = VipUserDto.builder()
                .intraId(user.getIntraId())
                .rank(user.getTotalExp() == 0 ? -1 : rank)
                .statusMessage(user.getStatusMessage())
                .level(ExpLevelCalculator.getLevel(user.getTotalExp()))
                .exp(user.getTotalExp())
                .build();
        return dto;
    }

    @Override
    public String toString() {
        return "VipUserDto{" +
                "intraId='" + intraId + '\'' +
                ", rank=" + rank +
                ", statusMessage='" + statusMessage + '\'' +
                ", level=" + level +
                ", exp=" + exp +
                '}';
    }
}
