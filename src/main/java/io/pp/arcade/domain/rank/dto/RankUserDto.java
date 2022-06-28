package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.domain.rank.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUserDto {
    private Integer id;
    private String intraId;
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private String statusMessage;
    private double winRate;

    public static RankUserDto from (RankRedis userRank, Integer rank){
         RankUserDto dto;
        /*
         if (userRank == null) {
            dto = RankUserDto.builder()
                    .intraId(null)
                    .ppp(0)
                    .wins(0)
                    .losses(0)
                    .winRate(0)
                    .rank(-1)
                    .statusMessage("")
                    .build();
        }*/
        dto = RankUserDto.builder()
                .id(userRank.getId())
                .intraId(userRank.getIntraId())
                .ppp(userRank.getPpp())
                .wins(userRank.getWins())
                .losses(userRank.getLosses())
                .winRate(userRank.getWinRate())
                .rank(rank == null ? -1 : rank.intValue())
                .statusMessage(userRank.getStatusMessage())
                .build();
        return dto;
    }
}
