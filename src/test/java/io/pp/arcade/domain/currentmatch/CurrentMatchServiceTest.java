package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.*;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@SpringBootTest
class CurrentMatchServiceTest {
    @Autowired
    CurrentMatchRepository currentMatchRepository;
    @Autowired
    CurrentMatchService currentMatchService;
    @Autowired
    SlotTeamUserRepository slotTeamUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TestInitiator initiator;

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
        initiator.letsgo();
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];

        slot = initiator.slots[0];

        team1 = slot.getTeam1();
        team2 = slot.getTeam2();

        game = gameRepository.save(Game.builder()
                .slot(slot)
                .season(1)
                .status(StatusType.LIVE)
                .build());
    }

    @Test
    @Transactional
    void addCurrentMatch() {
        CurrentMatchAddDto addDto = CurrentMatchAddDto.builder()
                .slot(SlotDto.from(slot))
                .user(UserDto.from(user1))
                .build();
        currentMatchService.addCurrentMatch(addDto);
        CurrentMatch match = currentMatchRepository.findByUserAndIsDel(user1, false).orElseThrow(() -> new BusinessException("E0001"));
        CurrentMatch match1 = currentMatchRepository.findByUserIdAndIsDel(user1.getId(), false).orElseThrow(() -> new BusinessException("E0001"));

        Assertions.assertThat(match.getUser()).isEqualTo(user1);
        Assertions.assertThat(match1.getUser()).isEqualTo(user1);
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
                .slot(SlotDto.from(slot))
                .isMatched(true)
                .matchImminent(false)
                .build();

        currentMatchService.modifyCurrentMatch(modifyDto);
        currentMatch = currentMatchRepository.findById(currentMatch.getId()).orElseThrow(() -> new BusinessException("E0001"));
        Assertions.assertThat(currentMatch.getIsMatched()).isEqualTo(true);
        Assertions.assertThat(currentMatch.getMatchImminent()).isEqualTo(false);
    }

    @Test
    @Transactional
    void saveGameInCurrentMatch() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isDel(false)
                .isMatched(false)
                .matchImminent(false)
                .build());
        CurrentMatchSaveGameDto saveGameDto = CurrentMatchSaveGameDto.builder()
                .game(GameDto.from(game))
                .build();
        slotTeamUserRepository.save(SlotTeamUser.builder().slot(slot).team(team1).user(user1).build());

        currentMatchService.saveGameInCurrentMatch(saveGameDto);
        currentMatch = currentMatchRepository.findById(currentMatch.getId()).orElseThrow(() -> new BusinessException("E0001"));
        Assertions.assertThat(currentMatch.getGame()).isEqualTo(game);
    }

    @Test
    @Transactional
    void findCurrentMatchByUserId() {
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .matchImminent(false)
                .isMatched(false)
                .isDel(false)
                .build());

        CurrentMatchDto dto1 = currentMatchService.findCurrentMatchByUser(UserDto.from(user1));
        CurrentMatchDto dto2 = currentMatchService.findCurrentMatchByUser(UserDto.from(user2));

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

        CurrentMatchRemoveDto removeDto = CurrentMatchRemoveDto.builder()
                .user(UserDto.from(user1)).build();

        Assertions.assertThat(currentMatchRepository.findAll()).isNotEqualTo(Collections.EMPTY_LIST);
        currentMatchService.removeCurrentMatch(removeDto);
        Assertions.assertThat(currentMatchRepository.findAllByIsDel(false)).isEqualTo(Collections.EMPTY_LIST);
    }
}