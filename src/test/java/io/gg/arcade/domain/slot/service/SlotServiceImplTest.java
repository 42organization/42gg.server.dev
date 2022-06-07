package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotAddRequestDto;
import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotRequestDto;
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
        Assertions.assertThat(slotRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(createdSlot.getTeam1Id()).isEqualTo(dto.getTeam1Id());
        Assertions.assertThat(createdSlot.getTeam2Id()).isEqualTo(dto.getTeam2Id());
        Assertions.assertThat(createdSlot.getTime()).isEqualTo(dto.getTime());
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
        SlotRequestDto slotDto = SlotRequestDto.builder()
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
        SlotRequestDto closeReqDto1 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(10).getId())
                .gamePpp(2000)
                .type("single")
                .build();
        SlotRequestDto closeReqDto2 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(11).getId())
                .gamePpp(1000)
                .type("double")
                .build();
        SlotRequestDto closeReqDto3 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(12).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotRequestDto closeReqDto4 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(12).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotRequestDto openReqDto1 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(1).getId())
                .gamePpp(1200)
                .type("single")
                .build();
        SlotRequestDto openReqDto2 = SlotRequestDto.builder()
                .slotId(slotRepository.findAll().get(2).getId())
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
        SlotFindDto findDto = SlotFindDto.builder()
                .localDateTime(LocalDateTime.of(2022, 6, 6, 12, 0, 0))
                .currentUserPpp(1000)
                .inquiringType("single")
                .build();
        List<SlotResponseDto> lst = slotService.filterSlots(findDto);
        //then
        Integer count = 0;
        for (SlotResponseDto resDto : lst) {
            if (resDto.getStatus().equals("open")) {
                count++;
            }
        }
        Assertions.assertThat(count).isEqualTo(15);
        Assertions.assertThat(lst.get(1).getStatus()).isEqualTo("open");
        Assertions.assertThat(lst.get(2).getStatus()).isEqualTo("open");
        Assertions.assertThat(lst.get(10).getStatus()).isEqualTo("close");
        Assertions.assertThat(lst.get(11).getStatus()).isEqualTo("close");
        Assertions.assertThat(lst.get(12).getStatus()).isEqualTo("close");
    }
}