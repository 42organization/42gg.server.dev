package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
class PChangeServiceTest {
    @Autowired
    PChangeRepository pChangeRepository;
    @Autowired
    PChangeService pChangeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    SlotRepository slotRepository;

    User user1;
    User user2;
    User user3;
    User user4;
    Team team1;
    Team team2;
    Slot slot;
    Game game;

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
                .type(GameType.DOUBLE)
                .time(LocalDateTime.now())
                .headCount(4)
                .build());
        game = gameRepository.save(Game.builder()
                .slot(slot)
                .team1(team1)
                .team2(team2)
                .time(slot.getTime())
                .type(slot.getType())
                .season(1)
                .status(StatusType.LIVE)
                .build());
    }

    @Test
    @Transactional
    void addPChange() {
        //given
        PChangeAddDto addDto = PChangeAddDto.builder()
                .gameId(game.getId())
                .userId(user1.getId())
                .pppChange(333)
                .pppResult(user1.getPpp() + 333)
                .build();

        //when
        pChangeService.addPChange(addDto);

        //then
        PChange pChange = pChangeRepository.findAll().get(0);
        Assertions.assertThat(pChange.getGame().getId()).isEqualTo(game.getId());
        Assertions.assertThat(pChange.getUser().getId()).isEqualTo(user1.getId());
        Assertions.assertThat(pChange.getPppChange()).isEqualTo(333);
        Assertions.assertThat(pChange.getPppResult()).isEqualTo(user1.getPpp() + 333);
    }

    @Test
    @Transactional
    void findPChange() {
    }
}