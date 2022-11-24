package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.domain.slot.SlotService;
import io.pp.arcade.v1.domain.slot.dto.SlotAddDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 하루에 한 두 번
@Component
public class SlotGenerator extends AbstractScheduler {
    private final SlotService slotService;

    public SlotGenerator(SlotService slotService) {
        this.slotService = slotService;
        this.setCron("30 0 0 * * *");
    }

    @Getter @Setter
    private Integer startTime = 10;
    @Getter @Setter
    private Integer interval = 1;
    @Getter @Setter
    private Integer slotNum = 240;

    public void dailyGenerate() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < slotNum; i++) {
            LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    startTime + i / (60 / interval), (i * interval) % 60, 0); // 3시부터 10분 간격으로 18개 슬롯 생성
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();
            slotService.addSlot(dto);
        }
    }

    @Override
    public Runnable runnable() {
        return () -> {
            dailyGenerate();
        };
    }
}
