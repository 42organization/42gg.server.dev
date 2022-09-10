package io.pp.arcade.domain.team;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.team.TeamService;
import io.pp.arcade.v1.domain.team.dto.*;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
class TeamServiceTest {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamService teamService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SlotTeamUserRepository slotTeamUserRepository;

    @Autowired
    TestInitiator testInitiator;

    User user1;
    User user2;

    Team team0;

    Team team1;

    Team team2;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];

        Slot slot1 = slotRepository.save(Slot.builder()
                .gamePpp(0)
                .type(GameType.SINGLE)
                .tableId(1)
                .time(LocalDateTime.now())
                .headCount(0)
                .build());
        Slot slot2 = slotRepository.save(Slot.builder()
                .gamePpp(0)
                .type(GameType.SINGLE)
                .tableId(1)
                .time(LocalDateTime.now())
                .headCount(0)
                .build());
        team0 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .slot(slot1)
                .headCount(0)
                .score(0)
                .build());
        team1 = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .slot(slot1)
                .headCount(1)
                .score(0)
                .build());
        team2 = teamRepository.save(Team.builder()
                .teamPpp((user1.getPpp() + user2.getPpp()) / 2)
                .slot(slot2)
                .headCount(2)
                .score(0)
                .build());
    }

    @Test
    @Transactional
    void findById() {
        //when
        TeamDto teamDto = teamService.findById(team0.getId());

        //then
        Assertions.assertThat(teamDto.getId()).isEqualTo(team0.getId());
    }

    @Test
    @Transactional
    void addUserInTeam() {
        //given
        TeamAddUserDto dto = TeamAddUserDto.builder()
                .teamId(team0.getId())
                .userId(user1.getId())
                .build();

        //when
        teamService.addUserInTeam(dto);
        Team team_0 = teamRepository.findById(team0.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(team_0.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(team_0.getTeamPpp()).isEqualTo(user1.getPpp());

        //given
        TeamAddUserDto dto1 = TeamAddUserDto.builder()
                .teamId(team0.getId())
                .userId(user2.getId())
                .build();

        //when
        teamService.addUserInTeam(dto1);
        Team team_1 = teamRepository.findById(team0.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(team_1.getHeadCount()).isEqualTo(2);
        Assertions.assertThat(team_1.getTeamPpp()).isEqualTo((user1.getPpp() + user2.getPpp()) / 2);
    }

    @Test
    @Transactional
    void removeUserInTeam() {
        //given
        TeamRemoveUserDto dto = TeamRemoveUserDto.builder()
                .slotId(team0.getSlot().getId())
                .userId(user1.getId())
                .build();
        team0.setHeadCount(2);
        team0.setTeamPpp((user1.getPpp() + user2.getPpp()) / 2);
        team0 = teamRepository.findById(team0.getId()).orElseThrow(() -> new BusinessException("E0001"));
        slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(team0.getSlot())
                .team(team0)
                .user(user1)
                .build());

        //when
        teamService.removeUserInTeam(dto);
        Team team = teamRepository.findById(team0.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(team.getHeadCount()).isEqualTo(1);
        Assertions.assertThat(team.getTeamPpp()).isBetween(user2.getPpp() - 1, user2.getPpp() + 1);
    }

    @Test
    @Transactional
    void saveGameResultInTeam() {
        //given
        TeamSaveGameResultDto dto = TeamSaveGameResultDto.builder()
                .teamId(team2.getId())
                .score(2)
                .win(true)
                .build();

        //when
        teamService.saveGameResultInTeam(dto);
        Team team = teamRepository.findById(team2.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(team.getScore()).isEqualTo(2);
        Assertions.assertThat(team.getWin()).isEqualTo(true);
    }

    @Test
    @Transactional
    void modifyGameResultInTeam() {
        //given
        Team team4 = teamRepository.save(Team.builder()
                .teamPpp(42)
                .headCount(1)
                .win(true)
                .score(5)
                .build());
        TeamModifyGameResultDto dto = TeamModifyGameResultDto.builder()
                .teamId(team4.getId())
                .score(1)
                .win(false)
                .build();

        //when
        teamService.modifyGameResultInTeam(dto);
        Team team = teamRepository.findById(team4.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(team.getScore()).isEqualTo(1);
        Assertions.assertThat(team.getWin()).isEqualTo(false);
    }
}