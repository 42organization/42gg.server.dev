package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.TeamRequestDto;
import io.gg.arcade.domain.team.dto.TeamResponseDto;
import io.gg.arcade.domain.team.entity.Team;

import java.util.List;

public interface TeamService {
    void addUserInTeam(TeamRequestDto teamDto);
    void modifyTeam(TeamRequestDto teamDto);
    TeamResponseDto findTeam();
    void removeUserInTeam();
}
