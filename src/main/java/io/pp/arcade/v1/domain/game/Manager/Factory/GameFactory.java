package io.pp.arcade.v1.domain.game.Manager.Factory;

import io.pp.arcade.v1.domain.game.Manager.data.GamePlayer;
import io.pp.arcade.v1.domain.game.Manager.data.GameTeam;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.v1.domain.team.dto.TeamDto;


public interface GameFactory {
    GameTeam getGameTeamDto();
    GamePlayer getGamePlayer(GameDto gameDto, SlotTeamUserDto slotTeamUser);
    void setGameTeamDto(GameTeam gameTeam, TeamDto teamDto);
}
