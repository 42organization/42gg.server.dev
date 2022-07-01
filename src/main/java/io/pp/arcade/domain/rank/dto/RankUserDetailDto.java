package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUserDetailDto {
    private String intraId;
    private Integer rank;
    private Integer ppp;
    private String statusMessage;
    private Integer wins;
    private Integer losses;
    private Double winRate;
}
