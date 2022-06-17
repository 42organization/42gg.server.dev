package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.util.GameType;
import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyPppDto {
    private String intraId;
    private GameType gameType;
    private Integer Ppp;
}
