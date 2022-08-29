package io.pp.arcade.domain.game.Manager.Factory;

import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.game.Manager.data.GamePlayer;
import io.pp.arcade.domain.game.Manager.data.GamePlayerNormal;
import io.pp.arcade.domain.game.Manager.data.GameTeam;
import io.pp.arcade.domain.game.Manager.data.GameTeamNormal;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GameNormalFactory implements GameFactory {

    public GameTeam getGameTeamDto() {
        return GameTeamNormal.builder().players(new ArrayList<>()).build();
    }

    public GamePlayer getGamePlayer(GameDto gameDto, SlotTeamUserDto slotTeamUser){
        return  GamePlayerNormal.builder()
                .intraId(slotTeamUser.getUser().getIntraId())
                .userImageUri(slotTeamUser.getUser().getImageUri())
                .level(0)
                .build();
    }

    @Override
    public void setGameTeamDto(GameTeam gameTeamDto, TeamDto teamDto) {
        return ;
    }
}
