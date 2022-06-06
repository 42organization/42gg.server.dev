package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class TeamResponseDto {
    private List<Team> teams;
    private Integer headCount;

    @Builder
    public TeamResponseDto(List<Team> teams, Integer headCount) {
        this.teams = teams;
        this.headCount = headCount;
    }
}
