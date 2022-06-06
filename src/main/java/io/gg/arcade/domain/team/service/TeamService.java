package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.game.dto.GameInfoDto;
import io.gg.arcade.domain.team.dto.TeamRequestDto;
import io.gg.arcade.domain.team.dto.TeamResponseDto;
import io.gg.arcade.domain.team.entity.Team;

import java.util.List;

public interface TeamService {
    Team addUserInTeam(TeamRequestDto teamDto);
    TeamResponseDto findTeam(TeamRequestDto teamDto);
    void removeUserInTeam(TeamRequestDto teamDto);
    void modifyTeamAfterGame(TeamRequestDto teamDto, GameInfoDto gameInfoDto);
}
