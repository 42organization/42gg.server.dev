package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RankDto {
    private Integer id;
    private UserDto user;
    private Integer seasonId;
    private Integer ranking;
    private GameType gameType;
    private RacketType racketType;
    private Integer wins;
    private Integer losses;
    private Integer ppp;
    private String statusMessage;

    public static RankDto from(Rank rank){
        RankDto rankDto = RankDto.builder()
                .id(rank.getId())
                .ppp(rank.getPpp())
                .losses(rank.getLosses())
                .wins(rank.getWins())
                .gameType(rank.getGameType())
                .ranking(rank.getRanking())
                .seasonId(rank.getSeasonId())
                .user(UserDto.from(rank.getUser()))
                .racketType(rank.getRacketType())
                .statusMessage(rank.getStatusMessage())
                .build();
        return rankDto;
    }

    @Override
    public String toString() {
        return "RankDto{" +
                "id=" + id +
                ", user=" + user +
                ", seasonId=" + seasonId +
                ", ranking=" + ranking +
                ", gameType=" + gameType +
                ", racketType=" + racketType +
                ", wins=" + wins +
                ", losses=" + losses +
                ", ppp=" + ppp +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
