package io.pp.arcade.v1.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRankResponseDto {
    private Integer rank;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
    private Double winRate;

    @Builder
    public UserRankResponseDto(Integer rank, Integer ppp, Integer wins, Integer losses, Double winRate) {
        this.rank = rank;
        this.ppp = ppp;
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
    }

    @Override
    public String toString() {
        return "UserRankResponseDto{" +
                "rank=" + rank +
                ", ppp=" + ppp +
                ", wins=" + wins +
                ", losses=" + losses +
                ", winRate=" + winRate +
                '}';
    }
}