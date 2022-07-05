package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TeamModifyGameResultDto {
    private Integer teamId;
    private Integer score;
    private Boolean win;
}
