package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class GameResultResponseDto {
    List<GameResultDto> games;
    Integer lastGameId;
    Integer currentPage;
    Integer totalPage;
}
