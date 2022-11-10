package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyDto {
    private UserDto userDto;
    private GameType gameType;
    private Integer modifyStatus;
    private Integer Ppp;

    @Override
    public String toString() {
        return "RankModifyDto{" +
                "intraId='" + userDto.getIntraId() + '\'' +
                ", gameType=" + gameType +
                ", modifyStatus=" + modifyStatus +
                ", Ppp=" + Ppp +
                '}';
    }
}
