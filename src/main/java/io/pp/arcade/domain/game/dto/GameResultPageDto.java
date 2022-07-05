package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class GameResultPageDto {
    List<GameDto> gameList;
    Integer currentPage;
    Integer totalPage;
}
