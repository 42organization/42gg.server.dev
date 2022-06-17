package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyPppDto {
    private String intraId;
    private String gameType;
    private Integer Ppp;
}
