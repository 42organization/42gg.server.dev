package io.gg.arcade.domain.pchange.service;

import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.game.entity.Game;
import io.gg.arcade.domain.game.repository.GameRepository;
import io.gg.arcade.domain.game.service.GameService;
import io.gg.arcade.domain.pchange.dto.PchangeAddRequestDto;
import io.gg.arcade.domain.pchange.dto.PchangeFindRequestDto;
import io.gg.arcade.domain.pchange.dto.PchangeFindResposeDto;
import io.gg.arcade.domain.pchange.repository.PchangeRepository;
import io.gg.arcade.domain.slot.entity.Slot;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.slot.service.SlotService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PchangeServiceTest {

    @Autowired
    GameService gameService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PchangeService pchangeService;

    @Autowired
    PchangeRepository pchangeRepository;

    @Autowired
    SlotService slotService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;
    Game game;
    Slot slot;

    @BeforeAll
    void init() {
        slotService.addTodaySlots();

        slot = slotRepository.findAll().get(0);

        GameAddRequestDto gameDto = GameAddRequestDto.builder()
                .team1Id(slot.getTeam1Id())
                .team2Id(slot.getTeam2Id())
                .build();
        gameService.addGame(gameDto);

        UserAddRequestDto userDto1 = UserAddRequestDto.builder()
                .intraId("nheo")
                .userImgUri("")
                .build();
        userService.addUser(userDto1);
        UserAddRequestDto userDto2 = UserAddRequestDto.builder()
                .intraId("donghyuk")
                .userImgUri("")
                .build();
        userService.addUser(userDto2);

        user1 = userRepository.findByIntraId("nheo").orElseThrow();
        user2 = userRepository.findByIntraId("donghyuk").orElseThrow();
        game = gameRepository.findAll().get(0);
    }

    @Test
    @Transactional
    void addPchange() {
        //given
        PchangeAddRequestDto pchangeDto = PchangeAddRequestDto.builder()
                .gameId(game.getId())
                .pppChange(10)
                .pppResult(user1.getPpp() + 10)
                .userId(user1.getId())
                .build();

        //when
        pchangeService.addPchange(pchangeDto);

        //then
        Assertions.assertThat(pchangeRepository.findAll().get(0).getPppResult()).isEqualTo(user1.getPpp() + 10);
    }

    @Test
    @Transactional
    void modifyPchange() {
    }

    @Test
    @Transactional
    void deletePchange() {
    }

    @Test
    @Transactional
    void findPchanges() {
        PchangeAddRequestDto pchangeDto1 = PchangeAddRequestDto.builder()
                .gameId(game.getId())
                .pppChange(10)
                .pppResult(user1.getPpp() + 10)
                .userId(user1.getId())
                .build();
        PchangeFindRequestDto pchangeDto2 = PchangeFindRequestDto.builder()
                .gameId(game.getId())
                .userId(user1.getId())
                .build();
        //when
        pchangeService.addPchange(pchangeDto1);

        PchangeFindResposeDto pdto = pchangeService.findPchanges(pchangeDto2);
        Assertions.assertThat(pdto.getPppResult()).isEqualTo(1010);
    }
}