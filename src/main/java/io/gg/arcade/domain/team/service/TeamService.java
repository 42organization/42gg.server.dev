package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.TeamAddUserRequestDto;

public interface TeamService {
    void addUserInTeam(TeamAddUserRequestDto dto);
    void removeUserInTeam();
}
