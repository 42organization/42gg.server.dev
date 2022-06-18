package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyDto {
    private String intraId;
    private GameType gameType;
    private Boolean isWin;
    private Integer Ppp;
}
