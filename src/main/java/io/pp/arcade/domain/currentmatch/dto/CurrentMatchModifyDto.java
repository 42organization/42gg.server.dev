package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.game.dto.GameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CurrentMatchModifyDto {
    private Integer userId;
    private Boolean isMatched;
    private Boolean matchImminent;
    private GameDto gameDto;
}
