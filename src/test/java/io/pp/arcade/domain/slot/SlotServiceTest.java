package io.pp.arcade.domain.slot;

import io.pp.arcade.domain.slot.dto.SlotAddRequestDto;
import io.pp.arcade.domain.slot.dto.SlotAddUserRequestDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
                .team1(team1)
                .team2(team2)
                .time(LocalDateTime.now())
                .headCount(0)
                .build());
    }

    @Test
    @Transactional
    void addSlot() {
        LocalDateTime time = LocalDateTime.now();
        SlotAddRequestDto dto = SlotAddRequestDto.builder().time(time).build();
        slotService.addSlot(dto);
        Slot slot1 = slotRepository.findAll().get(1);
        Assertions.assertThat(dto.getTime()).isEqualTo(slot1.getTime());
    }

    @Test
    @Transactional
    void addUserInSlot() {
        SlotAddUserRequestDto dto = SlotAddUserRequestDto.builder()
                .slotId(slot.getId())
                .type("single")
                .joinUserPpp(user1.getPpp())
                .build();
        slotService.addUserInSlot(dto);
        Slot s1 = slotRepository.findById(slot.getId()).orElseThrow();
        Assertions.assertThat(s1.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(s1.getGamePpp()).isEqualTo(user1.getPpp());
        Assertions.assertThat(s1.getType()).isEqualTo("single");

        SlotAddUserRequestDto dto1 = SlotAddUserRequestDto.builder()
                .slotId(slot.getId())
                .type("single")
                .joinUserPpp(user2.getPpp())
                .build();
        slotService.addUserInSlot(dto);
        Slot s2 = slotRepository.findById(slot.getId()).orElseThrow();
        Assertions.assertThat(s2.getHeadCount()).isEqualTo(2);
        Assertions.assertThat(s2.getGamePpp()).isEqualTo((user1.getPpp() + user2.getPpp()) / 2);
        Assertions.assertThat(s2.getType()).isEqualTo("single");
    }

    @Test
    @Transactional
    void removeUserInSlot() {
    }

    @Test
    @Transactional
    void findSlotById() {
    }

    @Test
    @Transactional
    void findSlotsStatus() {
    }
}