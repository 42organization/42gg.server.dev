package io.pp.arcade.domain.team;

import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamModifyUserRequestDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

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
    UserRepository userRepository;

    User user1;
    User user2;
    Team team;

    @BeforeEach
    void init() {
        userService.addUser("jiyun");
        userService.addUser("jiyun2");
        user1 = userRepository.findByIntraId("jiyun").orElseThrow();
        user2 = userRepository.findByIntraId("jiyun2").orElseThrow();
        team = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .build());
    }

    @Test
    @Transactional
    void findById() {
        TeamDto teamDto = teamService.findById(team.getId());
        Assertions.assertThat(teamDto.getId()).isEqualTo(team.getId());
    }

    @Test
    @Transactional
    void addUserInTeam() {
        TeamModifyUserRequestDto dto = TeamModifyUserRequestDto.builder()
                .teamId(team.getId())
                .userId(user1.getId())
                .build();
        teamService.addUserInTeam(dto);
        TeamDto teamDto = teamService.findById(team.getId());
        Assertions.assertThat(teamDto.getHeadCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    void removeUserInTeam() {
    }

    @Test
    @Transactional
    void saveGameResultInTeam() {
    }

    @Test
    @Transactional
    void modifyGameResultInTeam() {
    }
}