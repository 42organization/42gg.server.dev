package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamResponseDto {
    private List<Team> teams;
    private Integer headCount;
}
