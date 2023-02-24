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
        this.setCron("30 17 * * * *");
    }
//    @Getter @Setter
//    private Integer startTime = 0;
    @Getter @Setter
    private Integer interval = 15;
//    @Getter @Setter
//    private Integer slotNum = 4;
    @Getter @Setter
    private Integer futureTimeGap = 0;

    public void dailyGenerate() {
        Integer slotNum = Math.max(60 *(1 + futureTimeGap) / interval, 0);
        LocalDateTime now = LocalDateTime.now();q
        LocalDateTime standard = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0)
                .plusHours(1);
        for (int i = 0; i < slotNum; i++) {
            LocalDateTime time = standard.plusMinutes(interval * i);
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();
            slotService.addSlot(dto);
        }
        futureTimeGap = 0;
    }
    @Override
    public Runnable runnable() {
        return () -> {
            dailyGenerate();
        };
    }
}
