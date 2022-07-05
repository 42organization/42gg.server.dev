package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RankListRequestDto {
    private GameType type;
}
