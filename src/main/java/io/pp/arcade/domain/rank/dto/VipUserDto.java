package io.pp.arcade.domain.rank.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.dto.UserDto;
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
                .level(user.getTotalExp()) // 머시기 해야함
                .exp(user.getTotalExp()) // 머시기 해야함
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
