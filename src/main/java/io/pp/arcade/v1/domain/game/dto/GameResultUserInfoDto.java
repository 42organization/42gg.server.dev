package io.pp.arcade.v1.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameResultUserInfoDto {
    List<GameUserInfoDto> teams;
    Integer teamScore;
}
