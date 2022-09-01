package io.pp.arcade.domain.game.Manager;

import io.pp.arcade.domain.game.Manager.Factory.GameFactory;
import io.pp.arcade.domain.game.Manager.Factory.GameNormalFactory;
import io.pp.arcade.domain.game.Manager.Factory.GameRankFactory;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.game.dto.GameResultDto;
import io.pp.arcade.domain.game.Manager.data.GameTeam;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserService;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.Mode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class GameResponseManager {
    private final HashMap<Mode, GameFactory> gameFactories;
    private final SlotTeamUserService slotTeamUserService;

    public GameResponseManager(GameNormalFactory gameNormalFactory, GameRankFactory gameRankFactory, SlotTeamUserService slotTeamUserService) {
        gameFactories = new HashMap<>();
        this.gameFactories.put(Mode.NORMAL, gameNormalFactory);
        this.gameFactories.put(Mode.RANK, gameRankFactory);
        this.slotTeamUserService = slotTeamUserService;
    }

    /* 유저의 요청들*/
    public void putResultInGames(List<GameResultDto> gameResultList, List<GameDto> gameLists, UserDto curUser) {
        for (GameDto game : gameLists) {
            /* leftUser 설정 */
            List<SlotTeamUserDto> slotTeamUserDtos = slotTeamUserService.findAllBySlotId(game.getSlot().getId());
            UserDto leftUser = (curUser == null)  ? slotTeamUserDtos.get(0).getUser() : curUser;

            /* 모드별(Rank, Normal) Factory 선택 */
            GameFactory gameFactory = gameFactories.get(game.getMode());
            GameTeam myTeamDto = gameFactory.getGameTeamDto();
            GameTeam enemyTeamDto = gameFactory.getGameTeamDto();

            /* GameResult -> setGamePlayers */
            TeamDto enemyTeam = null;
            TeamDto myTeam = slotTeamUserService.findTeamBySlotAndUser(game.getSlot().getId(), leftUser.getId()).getTeam();
            for (SlotTeamUserDto slotTeamUserDto : slotTeamUserDtos) {
                if (myTeam.getId().equals(slotTeamUserDto.getTeam().getId())) {
                    myTeamDto.getPlayers().add(gameFactory.getGamePlayer(game, slotTeamUserDto));
                } else {
                    enemyTeam = slotTeamUserDto.getTeam();
                    enemyTeamDto.getPlayers().add(gameFactory.getGamePlayer(game, slotTeamUserDto));
                }
            }

            /* GameResult -> setGameTeams */
            gameFactory.setGameTeamDto( myTeamDto, myTeam);
            gameFactory.setGameTeamDto( enemyTeamDto, enemyTeam);

            gameResultList.add(GameResultDto.builder()
                    .gameId(game.getId())
                    .mode(game.getMode())
                    .team1(myTeamDto)
                    .team2(enemyTeamDto)
                    .type(game.getSlot().getType())
                    .mode(game.getMode())
                    .status(game.getStatus())
                    .time(game.getSlot().getTime())
                    .build());
        }
    }
}
