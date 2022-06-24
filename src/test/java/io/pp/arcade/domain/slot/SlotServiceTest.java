package io.pp.arcade.domain.slot;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;

import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.SlotStatusType;
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
    @Autowired
    TestInitiator testInitiator;

    Slot slot;
    User user1;
    User user2;
    Slot[] slots;
    User[] users;
    Team[] teams;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        teams = testInitiator.teams;
        users = testInitiator.users;
        slots = testInitiator.slots;
//        user1 = userRepository.findByIntraId("hakim").orElse(null);
//        user2 = userRepository.findByIntraId("jabae").orElse(null);
//        user3 = userRepository.findByIntraId("sujpark").orElse(null);
//        user4 = userRepository.findByIntraId("jihyukim").orElse(null);
//        team1 = teamRepository.save(Team.builder()
//                .teamPpp(0)
//                .headCount(0)
//                .score(0)
//                .build());
//        team2 = teamRepository.save(Team.builder()
//                .teamPpp(0)
//                .headCount(0)
//                .score(0)
//                .build());
    }

    @Test
    @Transactional
    void addSlot() {
        //given
        LocalDateTime time = LocalDateTime.now();
        SlotAddDto dto = SlotAddDto.builder().tableId(1).time(time).build();

        //when
        slotService.addSlot(dto);
        List<Slot> slots2 = slotRepository.findAllByTimeAfterOrderByTimeAsc(time.minusDays(1));
        Slot addedSlot = slots2.get(slots2.size() - 1);
        Team team1 = addedSlot.getTeam1();
        Team team2 = addedSlot.getTeam2();

        //then
        Assertions.assertThat(dto.getTime()).isEqualTo(addedSlot.getTime());
        Assertions.assertThat(team1).isNotEqualTo(null);
        Assertions.assertThat(team2).isNotEqualTo(null);
    }

    @Test
    @Transactional
    void addUserInSlot() {
        //given
        SlotAddUserDto dto = SlotAddUserDto.builder()
                .slotId(slot.getId())
                .type(GameType.SINGLE)
                .joinUserPpp(user1.getPpp())
                .build();

        //when
        slotService.addUserInSlot(dto);
        Slot s1 = slotRepository.findById(slot.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        //then
        Assertions.assertThat(s1.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(s1.getGamePpp()).isEqualTo(user1.getPpp());
        Assertions.assertThat(s1.getType()).isEqualTo(GameType.SINGLE);

        //given
        SlotAddUserDto dto1 = SlotAddUserDto.builder()
                .slotId(slot.getId())
                .type(GameType.SINGLE)
                .joinUserPpp(user2.getPpp())
                .build();

        //when
        slotService.addUserInSlot(dto1);
        Slot s2 = slotRepository.findById(slot.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));

        //then
        Assertions.assertThat(s2.getHeadCount()).isEqualTo(2);
        Assertions.assertThat(s2.getGamePpp()).isEqualTo((user1.getPpp() + user2.getPpp()) / 2);
        Assertions.assertThat(s2.getType()).isEqualTo(GameType.SINGLE);
    }

//    @Test
//    @Transactional
//    void removeUserInSlot() {
//        // given
//        Slot slot1 = slotRepository.save(Slot.builder().tableId(1).team1(team1).team2(team2).time(LocalDateTime.now()).headCount(2).gamePpp(80).type(GameType.SINGLE).build());
//        SlotRemoveUserDto dto = SlotRemoveUserDto.builder()
//                .slotId(slot1.getId())
//                .exitUserPpp(60)
//                .build();
//        // when
//        slotService.removeUserInSlot(dto);
//        Slot removedUserSlot = slotRepository.findById(slot1.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
//
//        // then
//        Assertions.assertThat(removedUserSlot.getGamePpp()).isEqualTo(100);
//        Assertions.assertThat(removedUserSlot.getHeadCount()).isEqualTo(1);
//        Assertions.assertThat(removedUserSlot.getType()).isEqualTo(GameType.SINGLE);
//
//        // when
//        slotService.removeUserInSlot(dto);
//        Slot removedUserSlot2 = slotRepository.findById(slot1.getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
//
//        // then
//        Assertions.assertThat(removedUserSlot2.getGamePpp()).isEqualTo(null);
//        Assertions.assertThat(removedUserSlot2.getHeadCount()).isEqualTo(0);
//        Assertions.assertThat(removedUserSlot2.getType()).isEqualTo(null);
//    }

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
//        for (int i = 0; i < 18; i++) {
//            Team team_a = teamRepository.save(Team.builder()
//                    .teamPpp(0)
//                    .headCount(0)
//                    .score(0)
//                    .build());
//            Team team_b = teamRepository.save(Team.builder()
//                    .teamPpp(0)
//                    .headCount(0)
//                    .score(0)
//                    .build());
//            LocalDateTime test = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
//                    21 + i / 6,(i * 10) % 60, 0);
//            SlotAddDto dto = SlotAddDto.builder().tableId(1).time(test).build();
//            slotService.addSlot(dto);
//        }
        List<Slot> slots =  slotRepository.findAll();
        slots.get(0).setType(GameType.BUNGLE);
        slots.get(0).setHeadCount(3);
        slots.get(1).setType(GameType.SINGLE);
        slots.get(1).setHeadCount(2);
        slots.get(2).setType(GameType.SINGLE);
        slots.get(2).setHeadCount(1);

        //when
        SlotFindStatusDto dto = SlotFindStatusDto.builder()
                .type(GameType.SINGLE)
                .currentTime(LocalDateTime.of(1999,10,10,0,0,0))
                .userId(user1.getId())
                .build();
        List<SlotStatusDto> responseDtos = slotService.findSlotsStatus(dto);

        //then
        Integer openCount = 0;
        Integer closeCount = 0;
        for (SlotStatusDto resDto : responseDtos) {
            System.out.println(resDto.getStatus());
            if (resDto.getStatus().equals(SlotStatusType.OPEN)) {
                openCount++;
            } else if (resDto.getStatus().equals(SlotStatusType.CLOSE)) {
                closeCount++;
            }
        }
        Assertions.assertThat(openCount).isEqualTo(16);
        Assertions.assertThat(closeCount).isEqualTo(2);
    }
}