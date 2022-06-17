package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUserDto {
    private String intraId;
    private Integer rank;
    private Integer ppp;
    private String statusMessage;
    private double winRate;
}
