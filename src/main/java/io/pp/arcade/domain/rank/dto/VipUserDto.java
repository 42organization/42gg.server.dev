package io.pp.arcade.domain.rank.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.util.ExpLevelCalculator;
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
                .rank(rank)
                .statusMessage(user.getStatusMessage())
                .level(ExpLevelCalculator.getLevel(user.getTotalExp() == null ? 0 : user.getTotalExp()))
                .exp(ExpLevelCalculator.getCurrentLevelMyExp(user.getTotalExp() == null ? 0 : user.getTotalExp()))
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
