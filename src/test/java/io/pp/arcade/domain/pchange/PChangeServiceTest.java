package io.pp.arcade.domain.pchange;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.pchange.PChange;
import io.pp.arcade.v1.domain.pchange.PChangeRepository;
import io.pp.arcade.v1.domain.pchange.PChangeService;
import io.pp.arcade.v1.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeListFindDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeListDto;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    TestInitiator initiator;

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
        initiator.letsgo();
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];

        slot = initiator.slots[0];
        slot.setMode(Mode.RANK);
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();

        game = gameRepository.save(Game.builder()
                .slot(slot)
                .season(1)
                .mode(slot.getMode())
                .status(StatusType.LIVE)
                .build());
    }

    @Test
    @Transactional
    void addPChange() {
        //given
        PChangeAddDto addDto = PChangeAddDto.builder()
                .game(GameDto.from(game))
                .user(UserDto.from(user1))
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
        for (int i = 0; i < 100; i++) {
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(user1)
                    .pppChange(333)
                    .pppResult(user1.getPpp() + 333)
                    .expChange(0)
                    .expResult(user1.getTotalExp())
                    .build());
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(user1)
                    .pppChange(333)
                    .pppResult(user2.getPpp() + 333)
                    .expChange(0)
                    .expResult(user1.getTotalExp())
                    .build());
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(user1)
                    .pppChange(333)
                    .pppResult(user3.getPpp() + 333)
                    .expChange(0)
                    .expResult(user1.getTotalExp())
                    .build());
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(user1)
                    .pppChange(333)
                    .pppResult(user4.getPpp() + 333)
                    .expChange(0)
                    .expResult(user1.getTotalExp())
                    .build());
        }

        PChangeListFindDto findDto = PChangeListFindDto.builder()
                .gameId(game.getId() + 1)
                .intraId(user1.getIntraId())
                .mode(game.getMode())
                .count(20)
                .season(1)
                .build();

        PChangeListDto pChangeDto =  pChangeService.findPChangeByUserIdAfterGameIdAndGameMode(findDto);
        Assertions.assertThat(pChangeDto.getPChangeList().size()).isEqualTo(20);
        System.out.println(pChangeDto);
        Assertions.assertThat(pChangeDto.getPChangeList().get(0).getPppChange()).isEqualTo(333);
    }
}
