package io.pp.arcade.domain.game.Manager.Factory;

import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.game.Manager.data.GamePlayer;
import io.pp.arcade.domain.game.Manager.data.GameTeam;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;


public interface GameFactory {
    GameTeam getGameTeamDto();
    GamePlayer getGamePlayer(GameDto gameDto, SlotTeamUserDto slotTeamUser);
    void setGameTeamDto(GameTeam gameTeam, TeamDto teamDto);
}
