package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.util.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyStatusMessageDto {
    private String intraId;
    private GameType gameType;
    private String statusMessage;
}
