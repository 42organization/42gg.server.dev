package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindRankDto {
    private UserDto user;
    private Integer ranking;
    private Integer ppp;

    public static FindRankDto from(Rank rank) {
        FindRankDto findRankDto =  FindRankDto.builder()
                .user(rank.getUser() == null ? null : UserDto.from(rank.getUser()))
                .ppp(rank.getPpp())
                .ranking(rank.getRanking())
                .build();
        return findRankDto;
    }
}
