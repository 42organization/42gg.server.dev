package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.util.GameType;
import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankFindDto {
    private String intraId;
    private GameType gameType;
}
