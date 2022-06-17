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
import io.pp.arcade.domain.user.dto.UserDto;
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
    private final NotiService notiService;
    private final String startTime = "15";
    private final String endTime = "18";
    private final String intervalTime = "5";

    @Scheduled(cron = "0 */" + intervalTime + " " + startTime + "-" + endTime + " * * *", zone = "Asia/Seoul") // 초 분 시 일 월 년 요일
    public void updateIsImminent() throws MessagingException {
        LocalDateTime now = LocalDateTime.now();
        now = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute() + 5, 0);

        SlotDto slot = slotService.findByTime(now);
        if (slot != null) {
            TeamDto team1 = slot.getTeam1();
            TeamDto team2 = slot.getTeam2();

            notiGenerater.addMatchNotisBySlot(slot);
        }
    }
}
