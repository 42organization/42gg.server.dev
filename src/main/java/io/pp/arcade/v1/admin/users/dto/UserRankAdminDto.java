package io.pp.arcade.v1.admin.users.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.pp.arcade.v1.domain.rank.dto.DtoSerialize;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRankAdminDto {
    private String intraId;
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private String statusMessage;
    @JsonSerialize(using = DtoSerialize.CustomDoubleSerializer.class)
    private Double winRate;

    public static UserRankAdminDto from (RankRedis userRank, Integer ranking){
        UserRankAdminDto dto = UserRankAdminDto.builder()
                .intraId(userRank.getIntraId())
                .ppp(userRank.getPpp())
                .wins(userRank.getWins())
                .losses(userRank.getLosses())
                .winRate((userRank.getWins() + userRank.getLosses()) == 0 ? 0 : (double)(userRank.getWins() * 10000 / (userRank.getWins() + userRank.getLosses())) / 100)
                .rank(ranking == null ? -1 : ranking.intValue())
                .statusMessage(userRank.getStatusMessage())
                .build();
        return dto;
    }

    public static UserRankAdminDto from (Rank rank) {
        Integer wins = rank.getWins();
        Integer losses = rank.getLosses();
        UserRankAdminDto dto = UserRankAdminDto.builder()
                .intraId(rank.getUser().getIntraId())
                .ppp(rank.getPpp())
                .wins(rank.getWins())
                .losses(rank.getLosses())
                .winRate((wins + losses) == 0 ? 0 : (double)(wins * 10000 / wins + losses) / 100)
                .rank(rank.getRanking())
                .statusMessage(rank.getUser().getStatusMessage())
                .build();
        return dto;
    }

    @Override
    public String toString() {
        return "RankUserDto{" +
                "intraId='" + intraId + '\'' +
                ", ranking=" + rank +
                ", ppp=" + ppp +
                ", wins=" + wins +
                ", losses=" + losses +
                ", statusMessage='" + statusMessage + '\'' +
                ", winRate=" + winRate +
                '}';
    }
}
