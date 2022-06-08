package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotAddRequestDto;
import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotModifyRequestDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class SlotServiceImplTest {

    @Autowired
    SlotService slotService;

    @Autowired
    SlotRepository slotRepository;

    @Test
    @Transactional
    void addTodaySlots() {
        slotService.addTodaySlots();
        Assertions.assertThat(slotRepository.findAll()).isNotEqualTo(Collections.emptyList());
    }

    @Test
    @Transactional
    void addSlot() {
        //given
        LocalDateTime now = LocalDateTime.now();
        String team1Id = String.valueOf(UUID.randomUUID());
        String team2Id = String.valueOf(UUID.randomUUID());
        LocalDateTime test = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                15 / 6, 10 % 60, 0);
        SlotAddRequestDto dto = SlotAddRequestDto.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .time(test)
                .build();
        //when
        slotService.addSlot(dto);
        Slot createdSlot = slotRepository.findAll().get(0);
        //then
        Assertions.assertThat(slotRepository.findAll()).isNotEqualTo(Collections.emptyList());
        Assertions.assertThat(slotRepository.findAll().size()).isNotEqualTo(0);
//        Assertions.assertThat(createdSlot.getTeam1Id()).isEqualTo(dto.getTeam1Id());
//        Assertions.assertThat(createdSlot.getTeam2Id()).isEqualTo(dto.getTeam2Id());
//        Assertions.assertThat(createdSlot.getTime()).isEqualTo(dto.getTime());
    }

    @Test
    @Transactional
    void addUserInSlot(){
        //given
        LocalDateTime now = LocalDateTime.now();
        String team1Id = String.valueOf(UUID.randomUUID());
        String team2Id = String.valueOf(UUID.randomUUID());
        LocalDateTime test = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                15 / 6, 10 % 60, 0);
        SlotAddRequestDto dto = SlotAddRequestDto.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .time(test)
                .build();
        slotService.addSlot(dto);
        //when
        SlotModifyRequestDto slotDto = SlotModifyRequestDto.builder()
                .slotId(slotRepository.findAll().get(0).getId())
                .gamePpp(null)
                .type(null)
                .build();

        slotService.addUserInSlot(slotDto);

        //then
        Assertions.assertThat(slotRepository.findAll().get(0).getHeadCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    void removeUserInSlot() {
    }

    @Test
    @Transactional
    void filterSlots() {
        //given
        slotService.addTodaySlots();

        List<Slot> slots = slotRepository.findAll();
        SlotModifyRequestDto closeReqDto1 = SlotModifyRequestDto.builder()
                .slotId(slots.get(10).getId())
                .gamePpp(2000)
                .type("single")
                .build();
        SlotModifyRequestDto closeReqDto2 = SlotModifyRequestDto.builder()
                .slotId(slots.get(11).getId())
                .gamePpp(1000)
                .type("double")
                .build();
        SlotModifyRequestDto closeReqDto3 = SlotModifyRequestDto.builder()
                .slotId(slots.get(12).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotModifyRequestDto closeReqDto4 = SlotModifyRequestDto.builder()
                .slotId(slots.get(12).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotModifyRequestDto openReqDto1 = SlotModifyRequestDto.builder()
                .slotId(slots.get(1).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotModifyRequestDto openReqDto2 = SlotModifyRequestDto.builder()
                .slotId(slots.get(2).getId())
                .gamePpp(800)
                .type("single")
                .build();
        //when
        slotService.addUserInSlot(closeReqDto1);
        slotService.addUserInSlot(closeReqDto2);
        slotService.addUserInSlot(closeReqDto3);
        slotService.addUserInSlot(closeReqDto4);
        slotService.addUserInSlot(openReqDto1);
        slotService.addUserInSlot(openReqDto2);
        LocalDateTime now = LocalDateTime.now().minusDays(1);

        SlotFindDto slotFindDto = SlotFindDto.builder()
                .localDateTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() , 23, 59, 59))
                .currentUserPpp(1000)
                .inquiringType("single")
                .build();

        List<SlotResponseDto> lst = slotService.filterSlots(slotFindDto);
        //then
        Integer count = 0;
        for (SlotResponseDto resDto : lst) {
            if (resDto.getStatus().equals("open")) {
                count++;
            }
        }
//        Assertions.assertThat(count).isEqualTo(15);
        Assertions.assertThat(lst.get(1).getStatus()).isEqualTo("open");
        Assertions.assertThat(lst.get(2).getStatus()).isEqualTo("open");
        Assertions.assertThat(lst.get(10).getStatus()).isEqualTo("close");
        Assertions.assertThat(lst.get(11).getStatus()).isEqualTo("close");
        Assertions.assertThat(lst.get(12).getStatus()).isEqualTo("close");
    }

}