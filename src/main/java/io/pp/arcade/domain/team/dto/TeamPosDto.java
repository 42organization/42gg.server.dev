package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TeamPosDto {
    TeamDto myTeam;
    TeamDto enemyTeam;
}
