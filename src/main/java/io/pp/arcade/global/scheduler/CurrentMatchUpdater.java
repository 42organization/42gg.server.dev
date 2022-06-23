package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiAddDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.util.NotiGenerater;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CurrentMatchUpdater {
    private final SlotService slotService;
    private final NotiGenerater notiGenerater;
    private final CurrentMatchService currentMatchService;
    private final String startTime = "12";
    private final String endTime = "18";
    private final String intervalTime = "5";

    @Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *", zone = "Asia/Seoul") // 초 분 시 일 월 년 요일
    public void updateIsImminent() throws MessagingException {
        LocalDateTime now = LocalDateTime.now();
        now = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0);
        now = now.plusMinutes(5);

        SlotDto slot = slotService.findByTime(now);
        if (slot == null) {
            return;
        }
        Integer maxHeadCount = GameType.SINGLE.equals(slot.getType()) ? 2 : 4;
        if (maxHeadCount.equals(slot.getHeadCount())) {
            TeamDto team1 = slot.getTeam1();
            TeamDto team2 = slot.getTeam2();

            currentMatchSetter(team1.getUser1());
            currentMatchSetter(team1.getUser2());
            currentMatchSetter(team2.getUser1());
            currentMatchSetter(team2.getUser2());
            notiGenerater.addMatchNotisBySlot(slot);
        }
    }

    private void currentMatchSetter(UserDto user) {

        if (user != null) {
            CurrentMatchModifyDto modifyDto = CurrentMatchModifyDto.builder()
                    .userId(user.getId())
                    .isMatched(true)
                    .matchImminent(true)
                    .build();

            currentMatchService.modifyCurrentMatch(modifyDto);
        }
    }
}
