package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankRedisFindDto {
    private UserDto user;
    private GameType gameType;
    private SeasonDto seasonDto;

    @Override
    public String toString() {
        return "RankFindDto{" +
                "user='" + user +
                ", gameType=" + gameType +
                ", season=" + seasonDto +
                '}';
    }
}
