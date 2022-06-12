package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.game.GameService;
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
public class CurrentMatchUpdater {
    private final GameService gameService;
    private final SlotService slotService;
    private final CurrentMatchService currentMatchService;
    private final String startTime = "15";
    private final String endTime = "18";
    private final String intervalTime = "10";

    @Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *") // 초 분 시 일 월 년 요일
    public void updateIsImminent() {
        SlotDto slot = slotService.findByTime(LocalDateTime.now());

        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();

        modifyCurrentMatch(team1.getUser1());
        modifyCurrentMatch(team1.getUser2());
        modifyCurrentMatch(team2.getUser1());
        modifyCurrentMatch(team2.getUser2());
    }

    public void modifyCurrentMatch(UserDto user) {
        if (user != null) {
            CurrentMatchDto matchDto = currentMatchService.findCurrentMatchByUserId(user.getId());
            CurrentMatchModifyDto modifyDto = CurrentMatchModifyDto.builder()
                    .userId(user.getId())
                    .isMatched(matchDto.getIsMatched())
                    .matchImminent(true)
                    .gameDto(matchDto.getGame()).build();
            currentMatchService.modifyCurrentMatch(modifyDto);
        }
    }

}
