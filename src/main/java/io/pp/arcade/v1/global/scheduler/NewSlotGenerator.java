package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.domain.slot.SlotService;
import io.pp.arcade.v1.domain.slot.dto.SlotAddDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class NewSlotGenerator extends AbstractScheduler {
    private final SlotService slotService;

    public NewSlotGenerator(SlotService slotService) {
        this.interval = 15;
        this.slotService = slotService;
        this.setCron("27 30 * * * *");
    }

    @Getter @Setter
    private Integer interval;

    public void hourlyGenerate() {
        System.out.println("check");
        LocalDateTime now = LocalDateTime.now();
        Integer slotNum = 60 / interval;
        for (int i = 0; i < slotNum; i++) {
            LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    now.getHour() + 1, (i * interval) % 60, 0);
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();
            slotService.addSlot(dto);
        }
    }

    @Override
    public Runnable runnable() {
        return () -> {
            hourlyGenerate();
        };
    }
}
