package io.pp.arcade.domain.game;

import io.pp.arcade.domain.game.dto.GameAddDto;
import io.pp.arcade.domain.game.dto.GameModifyStatusDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.SlotDto;
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
class GameServiceTest {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameService gameService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SlotRepository slotRepository;

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
                .user1(user1)
                .user2(user2)
                .headCount(2)
                .score(0)
                .build());
        team2 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .user1(user3)
                .user2(user4)
                .headCount(2)
                .score(0)
                .build());
        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .type("double")
                .time(LocalDateTime.now())
                .headCount(4)
                .build());
    }

    @Test
    @Transactional
    void addGame() {
        //given
        GameAddDto addDto = GameAddDto.builder()
                .slotDto(SlotDto.from(slot))
                .build();

        //when
        gameService.addGame(addDto);

        //then
        Assertions.assertThat(gameRepository.findAll().get(0).getTeam1()).isEqualTo(team1);
        Assertions.assertThat(gameRepository.findAll().get(0).getTeam2()).isEqualTo(team2);
    }

    @Test
    @Transactional
    void modifyGameStatus() {
        //given
        Game game = gameRepository.save(Game.builder()
                .team1(team1)
                .team2(team2)
                .slot(slot)
                .type(slot.getType())
                .time(slot.getTime())
                .status("live")
                .season(1) //season 추가
                .build());

        GameModifyStatusDto modifyDto = GameModifyStatusDto.builder()
                .gameId(game.getId())
                .status("end")
                .build();
        //when
        gameService.modifyGameStatus(modifyDto);
        Game game1 = gameRepository.findById(game.getId()).orElseThrow();

        //then
        Assertions.assertThat(game1.getStatus()).isEqualTo("end");
    }
}