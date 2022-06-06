package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<SlotResponseDto> findByDate(SlotFindDto slotFindDto) {
        List<Slot> slotList = slotRepository.findAllByCreatedDateAfter(slotFindDto.getLocalDateTime());
        List<SlotResponseDto> dtoList = new ArrayList<>();
//        Integer ppp = slotFindDto.getUser().getPpp();

        for (Slot slot : slotList) {
            dtoList.add(SlotResponseDto.builder()
                    .slotId(slot.getId())
                    .headCount(slot.getHeadCount())
                    .status(getStatus(slot, 1000))
                    .build());
        }
        return dtoList;
    }

    //ranking 추가후 ppp로직 추가!!
    private String getStatus(Slot slot, Integer ppp) {
        if (slot.getType() == null) {
            slot.setType("single");
        }
        String status = "opened";
//type을 어케든 해야함...
        if (slot.getType().equals("single")) {
            if (slot.getHeadCount() == 2) {
                status = "closed";
            }
        } else {
            if (slot.getHeadCount() == 4) {
                status = "closed";
            }
        }
        return status;
    }
}
