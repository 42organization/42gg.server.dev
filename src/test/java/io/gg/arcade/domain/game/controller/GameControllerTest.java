package io.gg.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gg.arcade.RestDocsConfiguration;
import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.game.service.GameService;
import io.gg.arcade.domain.slot.dto.SlotDto;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.slot.service.SlotService;
import io.gg.arcade.domain.team.service.TeamService;
import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SlotService slotService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @Autowired
    GameService gameService;

    UserDto user1;
    UserDto user2;
    UserDto user3;
    UserDto user4;
    SlotDto slot1;
    SlotDto slot2;

    @BeforeAll
    void init() {
        slotService.addTodaySlots();
        userService.addUser(UserAddRequestDto.builder()
                .intraId("nheo")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("donghyuk")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("hakim")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("jiyun")
                .userImgUri("")
                .build());
        user1 = userService.findByIntraId("nheo");
        user2 = userService.findByIntraId("donghyuk");
        user3 = userService.findByIntraId("hakim");
        user4 = userService.findByIntraId("jiyun");
        slot1 = slotService.findSlotById(slotRepository.findAll().get(0).getId());
        slot2 = slotService.findSlotById(slotRepository.findAll().get(1).getId());
    }

    @Test
    @Transactional
    void saveGameResult() throws Exception{
        Map<String, String> body = new HashMap<>();
        body.put("slotId", String.valueOf(slot2.getId()));
        body.put("gamePpp", String.valueOf(slot2.getGamePpp()));
        body.put("type", "single");

        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user2.getId()))
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user3.getId()))
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        GameAddRequestDto gamedto = GameAddRequestDto.builder()
                .team1Id(slot2.getTeam1Id())
                .team2Id(slot2.getTeam2Id())
                .build();

        gameService.addGame(gamedto);

        Map<String, String> body2 = new HashMap<>();
        body2.put("team1Score", "2");
        body2.put("team2Score", "1");
        body2.put("gameId", "1");
        mockMvc.perform(post("/pingpong/games/1/result").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user2.getId()))
                        .content(objectMapper.writeValueAsString(body2)))
                .andExpect(status().isOk())
                .andDo(document("save-game-result"));

        mockMvc.perform(get("/pingpong/games/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("game-result"));
    }

}