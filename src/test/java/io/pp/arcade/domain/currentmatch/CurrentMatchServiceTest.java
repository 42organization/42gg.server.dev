package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.game.dto.GameDto;
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
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrentMatchServiceTest {
    @Autowired
    CurrentMatchRepository currentMatchRepository;
    @Autowired
    CurrentMatchService currentMatchService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    TeamRepository teamRepository;

    User user1;
    User user2;
    User user3;
    User user4;
    Slot slot;
    Team team1;
    Team team2;
    Game game;

    @BeforeEach
    void init() {
        user1 = userRepository.save(User.builder()
                .intraId("nheo")
                .imageUri("")
                .ppp(1000)
                .statusMessage("")
                .build());
        user2 = userRepository.save(User.builder()
                .intraId("donghyuk")
                .imageUri("")
                .ppp(950)
                .statusMessage("")
                .build());
        team1 = teamRepository.save(Team.builder()
                .user1(user1)
                .headCount(1)
                .score(0)
                .teamPpp(user1.getPpp())
                .build());
        team2 = teamRepository.save(Team.builder()
                .user1(user2)
                .headCount(1)
                .score(0)
                .teamPpp(user2.getPpp())
                .build());
        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .time(LocalDateTime.now())
                .gamePpp(1000)
                .type(null)
                .headCount(2)
                .build());
        game = gameRepository.save(Game.builder()
                .team1(team1)
                .team2(team2)
                .slot(slot)
                .season(1)
                .status("live")
                .time(slot.getTime())
                .type("single")
                .build());
    }

    @Test
    @Transactional
    void addCurrentMatch() {
        CurrentMatchAddDto addDto = CurrentMatchAddDto.builder()
                .slot(SlotDto.from(slot))
                .userId(user1.getId())
                .build();
        currentMatchService.addCurrentMatch(addDto);
        CurrentMatch match = currentMatchRepository.findByUser(user1).orElseThrow();

        Assertions.assertThat(match.getUser()).isEqualTo(user1);
    }

    @Test
    @Transactional
    void modifyCurrentMatch() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .game(game)
                .slot(slot)
                .user(user1)
                .build());
        CurrentMatchModifyDto modifyDto = CurrentMatchModifyDto.builder()
                .gameDto(GameDto.from(game))
                .userId(user1.getId())
                .isMatched(true)
                .matchImminent(false)
                .build();

        currentMatchService.modifyCurrentMatch(modifyDto);
        currentMatch = currentMatchRepository.findById(currentMatch.getId()).orElseThrow();
        Assertions.assertThat(currentMatch.getIsMatched()).isEqualTo(true);
        Assertions.assertThat(currentMatch.getMatchImminent()).isEqualTo(false);
    }

    @Test
    @Transactional
    void saveGameInCurrentMatch() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .build());
        CurrentMatchSaveGameDto saveGameDto = CurrentMatchSaveGameDto.builder()
                .gameId(game.getId())
                .userId(user1.getId())
                .build();

        currentMatchService.saveGameInCurrentMatch(saveGameDto);
        currentMatch = currentMatchRepository.findById(currentMatch.getId()).orElseThrow();
        Assertions.assertThat(currentMatch.getGame()).isEqualTo(game);
    }

    @Test
    @Transactional
    void findCurrentMatchByUserId() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .build());

        CurrentMatchDto dto1 = currentMatchService.findCurrentMatchByUserId(user1.getId());
        CurrentMatchDto dto2 = currentMatchService.findCurrentMatchByUserId(user2.getId());

        Assertions.assertThat(dto1).isNotEqualTo(null);
        Assertions.assertThat(dto2).isEqualTo(null);
    }

    @Test
    @Transactional
    void removeCurrentMatch() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .build());

        Assertions.assertThat(currentMatchRepository.findAll()).isNotEqualTo(Collections.EMPTY_LIST);
        currentMatchService.removeCurrentMatch(user1.getId());
        Assertions.assertThat(currentMatchRepository.findAll()).isEqualTo(Collections.EMPTY_LIST);
    }
}