package io.pp.arcade.v1.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamPosDto {
    private TeamDto myTeam;
    private TeamDto enemyTeam;
}
