package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamSaveGameResultDto {
    private Integer teamId;
    private Integer score;
    private Boolean win;
}
