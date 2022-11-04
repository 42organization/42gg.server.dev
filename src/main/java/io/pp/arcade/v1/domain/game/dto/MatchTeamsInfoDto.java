package io.pp.arcade.v1.domain.game.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchTeamsInfoDto {
    GameResultUserInfoDto myTeam;
    GameResultUserInfoDto enemyTeam;
}
