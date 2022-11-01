package io.pp.arcade.v1.domain.rank.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.pp.arcade.v1.domain.rank.Rank;
import io.pp.arcade.v1.domain.rank.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUserDto {
    private String intraId;
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private String statusMessage;
    @JsonSerialize(using = DtoSerialize.CustomDoubleSerializer.class)
    private Double winRate;

    public static RankUserDto from (RankRedis userRank, Integer rank){
        RankUserDto dto = RankUserDto.builder()
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

    public static RankUserDto from (Rank rank) {
        Integer wins = rank.getWins();
        Integer losses = rank.getLosses();
        RankUserDto dto = RankUserDto.builder()
                .intraId(rank.getUser().getIntraId())
                .ppp(rank.getPpp())
                .wins(rank.getWins())
                .losses(rank.getLosses())
                .winRate((losses + wins) == 0 ? 0 : ((double)wins / (double)(losses + wins) * 100))
                .rank(rank.getRanking())
                .statusMessage(rank.getUser().getStatusMessage())
                .build();
        return dto;
    }
    @Override
    public String toString() {
        return "RankUserDto{" +
                "intraId='" + intraId + '\'' +
                ", rank=" + rank +
                ", ppp=" + ppp +
                ", wins=" + wins +
                ", losses=" + losses +
                ", statusMessage='" + statusMessage + '\'' +
                ", winRate=" + winRate +
                '}';
    }
}
