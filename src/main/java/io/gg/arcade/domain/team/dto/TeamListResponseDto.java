package io.gg.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamListResponseDto {
    String team1Id;
    String team2Id;
    Integer team1HeadCount;
    Integer team2HeadCount;
}
