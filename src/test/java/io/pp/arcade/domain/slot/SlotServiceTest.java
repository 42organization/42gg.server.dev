package io.pp.arcade.domain.slot;

import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;

import io.pp.arcade.global.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SlotServiceTest {

    @Autowired
    SlotRepository slotRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SlotService slotService;

    Slot slot;
    Team team1;
    User user1;
    User user2;
    Team team2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        user1 = userRepository.save(User.builder().intraId("jiyun1").statusMessage("").ppp(42).build());
        user2 = userRepository.save(User.builder().intraId("jiyun2").statusMessage("").ppp(24).build());
        user3 = userRepository.save(User.builder().intraId("nheo1").statusMessage("").ppp(60).build());
        user4 = userRepository.save(User.builder().intraId("nheo2").statusMessage("").ppp(30).build());
        team1 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .build());
        team2 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .build());
        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .time(LocalDateTime.now())
                .headCount(0)
                .build());
    }

    @Test
    @Transactional
    void addSlot() {
        //given
        LocalDateTime time = LocalDateTime.now();
        SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();

        //when
        slotService.addSlot(dto);
        Slot slot1 = slotRepository.findAll().get(1);

        //then
        Assertions.assertThat(dto.getTime()).isEqualTo(slot1.getTime());
    }

    @Test
    @Transactional
    void addUserInSlot() {
        //given
        SlotAddUserDto dto = SlotAddUserDto.builder()
                .slotId(slot.getId())
                .type("single")
                .joinUserPpp(user1.getPpp())
                .build();

        //when
        slotService.addUserInSlot(dto);
        Slot s1 = slotRepository.findById(slot.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        //then
        Assertions.assertThat(s1.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(s1.getGamePpp()).isEqualTo(user1.getPpp());
        Assertions.assertThat(s1.getType()).isEqualTo("single");

        //given
        SlotAddUserDto dto1 = SlotAddUserDto.builder()
                .slotId(slot.getId())
                .type("single")
                .joinUserPpp(user2.getPpp())
                .build();

        //when
        slotService.addUserInSlot(dto1);
        Slot s2 = slotRepository.findById(slot.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        //then
        Assertions.assertThat(s2.getHeadCount()).isEqualTo(2);
        Assertions.assertThat(s2.getGamePpp()).isEqualTo((user1.getPpp() + user2.getPpp()) / 2);
        Assertions.assertThat(s2.getType()).isEqualTo("single");
    }

    @Test
    @Transactional
    void removeUserInSlot() {
        // given
        Slot slot1 = slotRepository.save(Slot.builder().tableId(1).team1(team1).team2(team2).time(LocalDateTime.now()).headCount(2).gamePpp(80).type("single").build());
        SlotRemoveUserDto dto = SlotRemoveUserDto.builder()
                .slotId(slot1.getId())
                .exitUserPpp(60)
                .build();
        // when
        slotService.removeUserInSlot(dto);
        Slot removedUserSlot = slotRepository.findById(slot1.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        // then
        Assertions.assertThat(removedUserSlot.getGamePpp()).isEqualTo(100);
        Assertions.assertThat(removedUserSlot.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(removedUserSlot.getType()).isEqualTo("single");

        // when
        slotService.removeUserInSlot(dto);
        Slot removedUserSlot2 = slotRepository.findById(slot1.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        // then
        Assertions.assertThat(removedUserSlot2.getGamePpp()).isEqualTo(null);
        Assertions.assertThat(removedUserSlot2.getHeadCount()).isEqualTo(0);
        Assertions.assertThat(removedUserSlot2.getType()).isEqualTo(null);
    }

    @Test
    @Transactional
    void findSlotById() {
        //when
        SlotDto slotDto = slotService.findSlotById(slot.getId());

        //then
        Assertions.assertThat(slotDto.getId()).isEqualTo(slot.getId());
    }

    @Test
    @Transactional
    void findSlotsStatus() {
        //given
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 18; i++) {
            Team team_a = teamRepository.save(Team.builder()
                    .teamPpp(0)
                    .headCount(0)
                    .score(0)
                    .build());
            Team team_b = teamRepository.save(Team.builder()
                    .teamPpp(0)
                    .headCount(0)
                    .score(0)
                    .build());
            LocalDateTime test = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    21 + i / 6,(i * 10) % 60, 0);
            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(test).build();
            slotService.addSlot(dto);
        }
        List<Slot> slots =  slotRepository.findAll();
        slots.get(0).setType("double");
        slots.get(0).setHeadCount(3);
        slots.get(1).setType("single");
        slots.get(1).setHeadCount(2);
        slots.get(2).setType("single");
        slots.get(2).setHeadCount(1);

        //when
        SlotFindStatusDto dto = SlotFindStatusDto.builder()
                .type("single")
                .currentTime(LocalDateTime.of(1999,10,10,0,0,0))
                .userId(user1.getId())
                .build();
        List<SlotStatusDto> responseDtos = slotService.findSlotsStatus(dto);

        //then
        Integer openCount = 0;
        Integer closeCount = 0;
        for (SlotStatusDto resDto : responseDtos) {
            System.out.println(resDto.getStatus());
            if (resDto.getStatus().equals("open")) {
                openCount++;
            } else if (resDto.getStatus().equals("close")) {
                closeCount++;
            }
        }
        Assertions.assertThat(openCount).isEqualTo(17);
        Assertions.assertThat(closeCount).isEqualTo(2);
    }
}