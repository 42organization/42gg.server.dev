package io.pp.arcade.domain.game;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.GameService;
import io.pp.arcade.v1.domain.game.dto.*;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    TestInitiator initiator;

    Slot slot;
    Team team1;
    User user1;
    User user2;
    Team team2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        initiator.letsgo();
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];

        slot = initiator.slots[0];
    }

    @Test
    @Transactional
    void addGame() {
        //given
        slot.setType(GameType.SINGLE);
        GameAddDto addDto = GameAddDto.builder()
                .slotDto(SlotDto.from(slot))
                .build();

        //when
        gameService.addGame(addDto);

        //then
        List<Game> games = gameRepository.findAll();
        Assertions.assertThat(games.get(games.size() - 1).getSlot()).isEqualTo(slot);
    }

    @Test
    @Transactional
    void modifyGameStatus() {
        //given
        slot.setType(GameType.SINGLE);

        Game game = gameRepository.save(Game.builder()
                .slot(slot)
                .status(StatusType.LIVE)
                .season(1) //season 추가
                .build());

        GameModifyStatusDto modifyDto = GameModifyStatusDto.builder()
                .game(GameDto.from(game))
                .status(StatusType.END)
                .build();
        //when
        gameService.modifyGameStatus(modifyDto);
        Game game1 = gameRepository.findById(game.getId()).orElseThrow(() -> new BusinessException("E0001"));

        //then
        Assertions.assertThat(game1.getStatus()).isEqualTo(StatusType.END);
    }

    @Test
    @Transactional
    void findGamesAfterId() {
        slot.setType(GameType.SINGLE);

        Game game = gameRepository.save(Game.builder()
                .slot(slot)
                .status(StatusType.END)
                .mode(Mode.NORMAL)
                .season(1) //season 추가
                .build());

        Game game2 = gameRepository.save(Game.builder()
                .slot(slot)
                .status(StatusType.END)
                .mode(Mode.RANK)
                .season(1) //season 추가
                .build());

        GameFindDto findDto = GameFindDto.builder()
                .id(game2.getId())
                .status(StatusType.END)
                .count(10)
                .build();

        //when
        GameResultListDto pageDto = gameService.v1_findGamesAfterId(findDto);
        System.out.println(pageDto);
        //then
        Assertions.assertThat(pageDto.getGameList()).isNotEqualTo(Collections.EMPTY_LIST);
    }
}