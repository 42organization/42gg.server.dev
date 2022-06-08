package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamAddUserRequestDto {
    private String teamId;
    private Integer userId;
}
