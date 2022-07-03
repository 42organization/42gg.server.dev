package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.RacketType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@Builder
public class RankRedisDto {
    private Integer id;
    private String intraId;
    private Integer ranking;
    private RacketType racketType;
    private GameType gameType;
    private Integer wins;
    private Integer losses;
    private Integer ppp;
    private String statusMessage;

    public static RankRedisDto from(RankRedis rank, Integer ranking){
        RankRedisDto rankDto = RankRedisDto.builder()
                .id(rank.getId())
                .intraId(rank.getIntraId())
                .racketType(rank.getRacketType())
                .gameType(rank.getGameType())
                .ppp(rank.getPpp())
                .losses(rank.getLosses())
                .wins(rank.getWins())
                .ranking(ranking)
                .statusMessage(rank.getStatusMessage())
                .build();
        return rankDto;
    }
}
