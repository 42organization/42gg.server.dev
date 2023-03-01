package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import io.pp.arcade.v1.admin.slot.repository.SlotManagementRepository;
import io.pp.arcade.v1.admin.slot.service.SlotAdminService;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.SlotService;
import io.pp.arcade.v1.domain.slot.dto.SlotAddDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 하루에 한 두 번
@Component
public class SlotGenerator extends AbstractScheduler {
    private final SlotService slotService;
    private final SlotAdminService slotAdminService;
    private final SlotRepository slotRepository;

    public SlotGenerator(SlotService slotService, SlotAdminService slotAdminService, SlotRepository slotRepository) {
        this.slotService = slotService;
        this.slotAdminService = slotAdminService;
        this.slotRepository = slotRepository;
        this.setCron("0 0 0/1 * * *");
    }

    public void dailyGenerate() {
        SlotManagement nowSlotPolicy = slotAdminService.getNowSlotPolicy();
        Slot lastSlot = slotRepository.findFirstByOrderByTimeDesc().orElseThrow();
        LocalDateTime lastSlotTime = lastSlot.getTime();
        LocalDateTime startTime = LocalDateTime.of(lastSlotTime.getYear(), lastSlotTime.getMonth(), lastSlotTime.getDayOfMonth(),
                lastSlotTime.getHour(), 0, 0).plusHours(1);
        int needSlotNum = calcNeedSlotNum(startTime, nowSlotPolicy);

        for(int i = 0; i < needSlotNum; i++) {
            LocalDateTime time = startTime.plusMinutes(nowSlotPolicy.getGameInterval() * i);
            LocalDateTime endTime = time.plusMinutes(nowSlotPolicy.getGameInterval());
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).endTime(endTime).build();
            slotService.addSlot(dto);
        }
    }

    private int calcNeedSlotNum(LocalDateTime startTime, SlotManagement nowSlotPolicy) {
        int cnt = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime standard = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);
        LocalDateTime targetTime = standard.plusHours(nowSlotPolicy.getFutureSlotTime());

        while (targetTime.compareTo(startTime) > 0) {
            startTime = startTime.plusMinutes(nowSlotPolicy.getGameInterval());
            cnt++;
        }
        return cnt;
    }

    @Override
    public Runnable runnable() {
        return () -> {
            dailyGenerate();
        };
    }
}
