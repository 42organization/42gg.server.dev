package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankRedisFindDto {
    private UserDto userDto;
    private GameType gameType;

    @Override
    public String toString() {
        return "RankFindDto{" +
                "intraId='" + userDto.getIntraId() + '\'' +
                ", gameType=" + gameType +
                '}';
    }
}
