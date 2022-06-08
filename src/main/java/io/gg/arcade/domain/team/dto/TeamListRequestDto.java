package io.gg.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamListRequestDto {
    String team1Id;
    String team2Id;
}
