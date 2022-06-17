package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankDto {
    private UserDto user;
    private Integer seasonId;
    private Integer ranking;
    private RacketType racketType;
    private Integer wins;
    private Integer losses;
    private Integer ppp;

    public static RankDto from(Rank rank){
        RankDto rankDto = RankDto.builder()
                .ppp(rank.getPpp())
                .losses(rank.getLosses())
                .wins(rank.getWins())
                .ranking(rank.getRanking())
                .seasonId(rank.getSeasonId())
                .user(UserDto.from(rank.getUser()))
                .racketType(rank.getRacketType())
                .build();
        return rankDto;
    }
}
