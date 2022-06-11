package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameAddDto;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class GameGenerator {
    private final GameService gameService;
    private final SlotService slotService;
    private final CurrentMatchService currentMatchService;
    private final String startTime = "15";
    private final String endTime = "18";
    private final String intervalTime = "10";

    @Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *") // 초 분 시 일 월 년 요일
    public void addGame() {
        Integer maxHeadCount = 2;

        SlotDto slotDto = slotService.findByTime(LocalDateTime.now());
        if (slotDto.getType().equals("double")) {
            maxHeadCount = 4;
        }

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
        }
    }

    private void saveCurrentMatch(UserDto user, GameDto game) {
        if (user != null) {
            CurrentMatchSaveGameDto matchSaveGameDto = CurrentMatchSaveGameDto.builder()
                    .gameId(game.getId())
                    .userId(user.getId())
                    .build();
            currentMatchService.SaveGameInCurrentMatch(matchSaveGameDto);
        }
    }
}
