package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RankModifyPppDto {
    private String intraId;
    private String gameType;
    private Integer Ppp;
}
