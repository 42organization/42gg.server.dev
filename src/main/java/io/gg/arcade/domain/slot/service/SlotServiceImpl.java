package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;

    @Override
    @Scheduled(cron = "0 0 0 1 * * *") // 나중에 global에 뺴야함
    public void addTodaySlots() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 18; i++) {
            addSlot(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0));
        }
    }

    @Override
    public Slot addSlot(LocalDateTime time) {
        Slot slot = Slot.builder()
                .team1Id(String.valueOf(UUID.randomUUID()))
                .team2Id(String.valueOf(UUID.randomUUID()))
                .time(time)
                .headCount(0)
                .build();
        return slotRepository.save(slot);
    }

    @Override
    public void addUserInSlot(SlotRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        if (slot.getHeadCount() == 0) {
            slot.setType(slotDto.getType());
            slot.setGamePpp(slotDto.getGamePpp());
        }
        slot.setHeadCount(slot.getHeadCount() + 1);
    }

    @Override
    public void removeUserInSlot(SlotRequestDto slotDto) {
        Slot slot = slotRepository.getById(slotDto.getSlotId());
        slot.setHeadCount(slot.getHeadCount() - 1);
        if (slot.getHeadCount() == 0) {
            slot.setType(null);
            slot.setGamePpp(null);
        }
    }
}
