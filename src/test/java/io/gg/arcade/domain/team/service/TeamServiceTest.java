package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.game.dto.GameInfoDto;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.service.SlotService;
import io.gg.arcade.domain.team.dto.TeamRequestDto;
import io.gg.arcade.domain.team.dto.TeamResponseDto;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.dto.UserSaveRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    SlotService slotService;

    User user1;
    User user2;
    Slot slot;

    @BeforeEach
    void init() {
        UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder()
                .intraId("nheo")
                .userImageUri("")
                .racketType("pen")
                .statusMessage("Hello Pingpong!")
                .build();
        user1 = userService.addUser(userSaveRequestDto);
        UserSaveRequestDto userSaveRequestDto2 = UserSaveRequestDto.builder()
                .intraId("hakim")
                .userImageUri("")
                .racketType("pen")
                .statusMessage("Hello Pingpong!")
                .build();
        user2 = userService.addUser(userSaveRequestDto2);
        slot = slotService.addSlot(LocalDateTime.now());
    }

    @Test
    @Transactional
    @DisplayName("팀에 유저 넣기")
    void addUserInTeam() {
       //given
        TeamRequestDto teamRequestDto1 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user1)
                .build();
        TeamRequestDto teamRequestDto2 = TeamRequestDto.builder()
                .teamId(slot.getTeam2Id())
                .user(user2)
                .build();
        //when
        Team team1 = teamService.addUserInTeam(teamRequestDto1);
        Team team2 = teamService.addUserInTeam(teamRequestDto2);

        //then
        Assertions.assertThat(team1).isEqualTo(teamRepository.findByTeamId(teamRequestDto1.getTeamId()).get(0));
        Assertions.assertThat(team2).isEqualTo(teamRepository.findByTeamId(teamRequestDto2.getTeamId()).get(0));
    }

    @Test
    @Transactional
    @DisplayName("경기 결과 입력 후 팀 정보 변경")
    void modifyTeamAfterGame() {
        //given
        TeamRequestDto teamRequestDto1 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user1)
                .build();
        TeamRequestDto teamRequestDto2 = TeamRequestDto.builder()
                .teamId(slot.getTeam2Id())
                .user(user2)
                .build();
        GameInfoDto gameInfoDto = GameInfoDto.builder()
                .team1Id(slot.getTeam1Id())
                .team2Id(slot.getTeam2Id())
                .team1Score(2)
                .team2Score(1)
                .team1Ppp(2200)
                .team2Ppp(2400)
                .build();
        Team team1 = teamService.addUserInTeam(teamRequestDto1);
        Team team2 = teamService.addUserInTeam(teamRequestDto2);

        //when
        teamService.modifyTeamAfterGame(teamRequestDto1, gameInfoDto);
        teamService.modifyTeamAfterGame(teamRequestDto2, gameInfoDto);

        //then
        Assertions.assertThat(team1.getIsWin()).isEqualTo(true);
        Assertions.assertThat(team1.getScore()).isEqualTo(2);
        Assertions.assertThat(team2.getIsWin()).isEqualTo(false);
        Assertions.assertThat(team2.getScore()).isEqualTo(1);
        System.out.println("team1 = " + team1.getPppChange());
        System.out.println("team2 = " + team2.getPppChange());
    }

    @Test
    @Transactional
    @DisplayName("팀 id로 팀 조회")
    void findTeam() {

        //given
        TeamRequestDto teamRequestDto1 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user1)
                .build();
        TeamRequestDto teamRequestDto2 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user2)
                .build();
        Team team1 = teamService.addUserInTeam(teamRequestDto1);
        Team team2 = teamService.addUserInTeam(teamRequestDto2);

        //when
        TeamResponseDto teamResponseDto = teamService.findTeam(teamRequestDto1);

        //then
        Assertions.assertThat(team1).isEqualTo(teamRepository.findByTeamId(teamRequestDto1.getTeamId()).get(0));
        Assertions.assertThat(team2).isEqualTo(teamRepository.findByTeamId(teamRequestDto1.getTeamId()).get(1));
    }

    @Test
    @Transactional
    @DisplayName("매칭 취소")
    void removeUserInTeam() {
        TeamRequestDto teamRequestDto1 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user1)
                .build();
        TeamRequestDto teamRequestDto2 = TeamRequestDto.builder()
                .teamId(slot.getTeam1Id())
                .user(user2)
                .build();
        Team team1 = teamService.addUserInTeam(teamRequestDto1);
        Team team2 = teamService.addUserInTeam(teamRequestDto2);

        teamService.removeUserInTeam(teamRequestDto1);
        Assertions.assertThat(team2).isEqualTo(teamRepository.findByTeamId(teamRequestDto2.getTeamId()).get(0));
        teamService.removeUserInTeam(teamRequestDto2);
        Assertions.assertThat(teamRepository.findByTeamId(teamRequestDto2.getTeamId())).isEqualTo(Collections.emptyList());
    }
}