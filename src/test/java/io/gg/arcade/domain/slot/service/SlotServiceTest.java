package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SlotServiceTest {
    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotService slotService;


    @Test
    @Transactional
    @DisplayName("슬롯 추가")
    void addSlot() {
        //given
        Slot slot1 = slotService.addSlot(LocalDateTime.now());
        //when
        Slot slot2 = slotRepository.getById(slot1.getId());
        //then
        Assertions.assertThat(slot1).isEqualTo(slot2);
    }

    @Test
    @Transactional
    @DisplayName("슬롯 자동생성")
    void testAddTodaySlots() {
        slotService.addTodaySlots();
        for (int i = 1; i <= 18; i++)
            System.out.println(slotRepository.getById(i).getTime());
    }

    @Test
    @Transactional
    @DisplayName("유저가 슬롯에 입장")
    void modifySlot() {
        //given
        SlotRequestDto slotDto1 = new SlotRequestDto();
        SlotRequestDto slotDto2 = new SlotRequestDto();
        Slot slot1 = slotService.addSlot(LocalDateTime.now());
        slotDto1.setSlotId(slot1.getId());
        slotDto1.setGamePpp(2400);
        slotDto1.setType("single");
        slotDto2.setSlotId(slot1.getId());
        slotDto2.setGamePpp(1000);
        slotDto2.setType("single");

        //when
        slotService.modifySlot(slotDto1);
        Slot slot2 = slotRepository.getById(slot1.getId());
        slotService.modifySlot(slotDto2);
        Slot slot3 = slotRepository.getById(slot1.getId());

        //then
        Assertions.assertThat(slot1.getGamePpp()).isEqualTo(slot2.getGamePpp()); // modify ppp test
        Assertions.assertThat(slot1.getGamePpp()).isEqualTo(slot3.getGamePpp()); // modify 안된거 있는지
        Assertions.assertThat(slot2.getGamePpp()).isEqualTo(slot3.getGamePpp());
    }
}