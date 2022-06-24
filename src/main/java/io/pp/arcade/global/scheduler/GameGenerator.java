package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameAddDto;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.noti.dto.NotiCanceledDto;
import io.pp.arcade.domain.noti.dto.NotiCanceledTypeDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.NotiType;
import io.pp.arcade.global.util.NotiGenerater;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class GameGenerator {
    private final GameService gameService;
    private final SlotService slotService;
    private final CurrentMatchService currentMatchService;
    private final NotiGenerater notiGenerater;
    private final String startTime = "15";
    private final String endTime = "18";
    private final String intervalTime = "10";

    @Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *", zone = "Asia/Seoul") // 초 분 시 일 월 년 요일
    public void addGame() throws MessagingException {
        Integer maxHeadCount = 2;
        LocalDateTime now = LocalDateTime.now();
        now = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0);
        SlotDto slotDto = slotService.findByTime(now);
        if (slotDto != null && GameType.BUNGLE.equals(slotDto.getType())) {
            maxHeadCount = 4;
        }
        if (slotDto != null) {
            CurrentMatchSaveGameDto matchSaveGameDto;
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
                NotiCanceledTypeDto canceledDto =  NotiCanceledTypeDto.builder().slotDto(slotDto).notiType(NotiType.CANCELEDBYTIME).build();
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
}
