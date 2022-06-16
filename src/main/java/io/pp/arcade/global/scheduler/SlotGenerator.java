package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotAddDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 하루에 한 두 번
@Component
@AllArgsConstructor
public class SlotGenerator {
    private final SlotService slotService;

    @Scheduled(cron = "30 0 0 * * *", zone = "Asia/Seoul") // 초 분 시 일 월 년 요일
    public void dailyGenerate() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 18; i++) {
            LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0); // 3시부터 10분 간격으로 18개 슬롯 생성
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();
            slotService.addSlot(dto);
        }
    }
}
