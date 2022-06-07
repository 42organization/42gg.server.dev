package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.TeamAddUserRequestDto;
import io.gg.arcade.domain.team.dto.TeamRemoveUserRequestDto;

public interface TeamService {
    void addUserInTeam(TeamAddUserRequestDto dto);
    void removeUserInTeam(TeamRemoveUserRequestDto dto);
}
