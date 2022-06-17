package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.util.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RankListRequestDto {
    private GameType type;
}
