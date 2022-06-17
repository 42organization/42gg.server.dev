package io.pp.arcade.domain.game.dto;

import lombok.Getter;

@Getter
public class GameFindRequestDto {
    Integer count;
    Integer gameId;
    String status;
}
