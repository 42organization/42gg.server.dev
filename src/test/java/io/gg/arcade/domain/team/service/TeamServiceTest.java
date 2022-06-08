package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.slot.service.SlotService;
import io.gg.arcade.domain.team.dto.TeamAddUserRequestDto;
import io.gg.arcade.domain.team.dto.TeamDto;
import io.gg.arcade.domain.team.dto.TeamRemoveUserRequestDto;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import io.gg.arcade.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TeamServiceTest {

    @Autowired
    TeamService teamService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SlotService slotService;

    @Autowired
    SlotRepository slotRepository;


    User user1;
    User user2;
    Slot slot;

    @BeforeAll
    void init() {
        slotService.addTodaySlots();

        slot = slotRepository.findAll().get(0);

        UserAddRequestDto userDto1 = UserAddRequestDto.builder()
                .intraId("nheo")
                .userImgUri("")
                .build();
        UserAddRequestDto userDto2 = UserAddRequestDto.builder()
                .intraId("donghyuk")
                .userImgUri("")
                .build();
//        userService.addUser(userDto1);
//        userService.addUser(userDto2);

        user1 = userRepository.findByIntraId("nheo").orElseThrow();
        user2 = userRepository.findByIntraId("donghyuk").orElseThrow();
    }

    @Test
    @Transactional
    void addUserInTeam() {
        TeamAddUserRequestDto dto = TeamAddUserRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .userId(user1.getId())
                .build();

        teamService.addUserInTeam(dto);

        Assertions.assertThat(teamRepository.findAll().get(0).getTeamId()).isEqualTo(slot.getTeam1Id());
    }

    @Test
    @Transactional
    void removeUserInTeam() {
        TeamAddUserRequestDto dto = TeamAddUserRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .userId(user1.getId())
                .build();

        teamService.addUserInTeam(dto);

        TeamDto team = teamService.findTeamByTeamId(slot.getTeam1Id());

        Assertions.assertThat(team.getHeadCount()).isEqualTo(1);

        TeamRemoveUserRequestDto dto2 = TeamRemoveUserRequestDto.builder()
                .teamId(team.getTeamId())
                .userId(user1.getId())
                .build();
        teamService.removeUserInTeam(dto2);

        TeamDto team2 = teamService.findTeamByTeamId(slot.getTeam1Id());

        Assertions.assertThat(team2.getHeadCount()).isEqualTo(0);
    }
}