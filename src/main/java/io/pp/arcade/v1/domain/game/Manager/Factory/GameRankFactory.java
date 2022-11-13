package io.pp.arcade.v1.domain.game.Manager.Factory;


import io.pp.arcade.v1.domain.game.Manager.data.GamePlayer;
import io.pp.arcade.v1.domain.game.Manager.data.GamePlayerRank;
import io.pp.arcade.v1.domain.game.Manager.data.GameTeam;
import io.pp.arcade.v1.domain.game.Manager.data.GameTeamRank;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.pchange.PChangeService;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankRedisFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.v1.domain.team.dto.TeamDto;
import io.pp.arcade.v1.global.type.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class GameRankFactory implements GameFactory{
    private final PChangeService pChangeService;
    private final RankRedisService rankRedisService;

    public GameTeam getGameTeamDto() {
        return GameTeamRank.builder().players(new ArrayList<>()).build();
    }

    public GamePlayer getGamePlayer(GameDto gameDto, SlotTeamUserDto slotTeamUser){
        GamePlayer gamePlayerDto;

        RankUserDto rankUserDto = rankRedisService.findRankById(RankRedisFindDto.builder()
                .gameType(gameDto.getSlot().getType())
                .user(slotTeamUser.getUser()).build());
        PChangeDto pChangeDto = gameDto.getStatus() == StatusType.END ?
                pChangeService.findPChangeByUserAndGame(PChangeFindDto.builder().game(gameDto).user(slotTeamUser.getUser()).build())
                : PChangeDto.builder().user(slotTeamUser.getUser()).game(gameDto).pppChange(0).pppResult(0).expChange(0).expResult(0).build();
        gamePlayerDto = GamePlayerRank.builder()
                .intraId(slotTeamUser.getUser().getIntraId())
                .userImageUri(slotTeamUser.getUser().getImageUri())
                .pppChange(pChangeDto.getPppChange())
                .losses(rankUserDto.getLosses())
                .winRate(rankUserDto.getWinRate())
                .wins(rankUserDto.getWins())
                .build();
        return gamePlayerDto;
    }

    @Override
    public void setGameTeamDto(GameTeam gameTeamDto, TeamDto teamDto) {
        GameTeamRank gameTeamRank = (GameTeamRank) gameTeamDto;
        gameTeamRank.setIsWin(teamDto.getWin());
        gameTeamRank.setScore(teamDto.getScore());
    }
}
