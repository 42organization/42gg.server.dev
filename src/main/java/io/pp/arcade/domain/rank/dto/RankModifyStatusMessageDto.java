package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyStatusMessageDto {
    private String intraId;
    private String statusMessage;
}
