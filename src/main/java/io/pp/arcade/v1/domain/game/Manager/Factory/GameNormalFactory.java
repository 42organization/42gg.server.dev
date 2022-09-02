package io.pp.arcade.v1.domain.game.Manager.Factory;


import io.pp.arcade.v1.domain.game.Manager.data.GamePlayer;
import io.pp.arcade.v1.domain.game.Manager.data.GamePlayerNormal;
import io.pp.arcade.v1.domain.game.Manager.data.GameTeam;
import io.pp.arcade.v1.domain.game.Manager.data.GameTeamNormal;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.v1.domain.team.dto.TeamDto;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
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
                .level(ExpLevelCalculator.getLevel(slotTeamUser.getUser().getTotalExp()))
                .build();
    }

    @Override
    public void setGameTeamDto(GameTeam gameTeamDto, TeamDto teamDto) {
        return ;
    }
}
