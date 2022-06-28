package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameAddDto;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.noti.dto.NotiCanceledTypeDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.NotiType;
import io.pp.arcade.global.util.NotiGenerater;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import java.time.LocalDateTime;

//private String cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *";
//private String cron = "0 */10 15-18 * * *";
@Component
public class GameGenerator extends AbstractScheduler {
    private final GameService gameService;
    private final SlotService slotService;

    public GameGenerator(GameService gameService, SlotService slotService, CurrentMatchService currentMatchService, NotiGenerater notiGenerater) {
        this.gameService = gameService;
        this.slotService = slotService;
        this.currentMatchService = currentMatchService;
        this.notiGenerater = notiGenerater;
        this.setCron("0 */10 15-18 * * *");
    }

    private final CurrentMatchService currentMatchService;
    private final NotiGenerater notiGenerater;

    public void addGame() throws MessagingException {
        Integer maxHeadCount = 2;
        LocalDateTime now = LocalDateTime.now();
        now = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0);
        SlotDto slotDto = slotService.findByTime(now);
        if (slotDto != null && GameType.BUNGLE.equals(slotDto.getType())) {
            maxHeadCount = 4;
        }
        if (slotDto != null) {
            if (slotDto.getHeadCount().equals(maxHeadCount)) {
                TeamDto team1 = slotDto.getTeam1();
                TeamDto team2 = slotDto.getTeam2();

                GameAddDto gameAddDto = GameAddDto.builder()
                        .slotDto(slotDto)
                        .build();
                gameService.addGame(gameAddDto);
                GameDto game = gameService.findBySlot(slotDto.getId());

                saveCurrentMatch(team1.getUser1(), game);
                saveCurrentMatch(team1.getUser2(), game);
                saveCurrentMatch(team2.getUser1(), game);
                saveCurrentMatch(team2.getUser2(), game);
            } else {
                NotiCanceledTypeDto canceledDto = NotiCanceledTypeDto.builder().slotDto(slotDto).notiType(NotiType.CANCELEDBYTIME).build();
                notiGenerater.addCancelNotisBySlot(canceledDto);
            }
        }
    }

    private void saveCurrentMatch(UserDto user, GameDto game) {
        if (user != null) {
            CurrentMatchSaveGameDto matchSaveGameDto = CurrentMatchSaveGameDto.builder()
                    .gameId(game.getId())
                    .userId(user.getId())
                    .build();
            currentMatchService.saveGameInCurrentMatch(matchSaveGameDto);
        }
    }

    @Override
    public Runnable runnable() {
        return () -> {
            try {
                addGame();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };
    }
}
