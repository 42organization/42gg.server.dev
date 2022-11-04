package io.pp.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.GameService;
import io.pp.arcade.v1.domain.game.dto.GameAddDto;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
public class v1GameControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameService gameService;
    @Autowired
    private CurrentMatchService currentMatchService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotTeamUserRepository slotTeamUserRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestInitiator initiator;

    @BeforeEach
    void setUp() {
        initiator.letsgo();
    }

    private GameDto addGame(SlotDto slotDto) {
        GameAddDto gameAddDto = GameAddDto.builder()
                .slotDto(slotDto)
                .build();
        gameService.addGame(gameAddDto);
        GameDto game = gameService.findBySlot(slotDto.getId());

        CurrentMatchSaveGameDto matchSaveGameDto = CurrentMatchSaveGameDto.builder()
                .game(game)
                .build();
        currentMatchService.saveGameInCurrentMatch(matchSaveGameDto);

        return game;
    }



    @Test
    @Transactional
    @DisplayName("경험치 결과 - 랭크 (/games/{gameId}/result)")
    void gameExpResult() throws Exception {

        User[] users = initiator.users;
        Slot[] slots = initiator.slots;

        Map<String, String> body = new HashMap<>();
        Slot slot;
        User user;

        {
            slot = slots[0];
            user = users[0];
            Integer currentExp = ExpLevelCalculator.getCurrentLevelMyExp(user.getTotalExp());
            Integer currentLevel = ExpLevelCalculator.getLevel(user.getTotalExp());
            body.put("slotId", slot.getId().toString());
            body.put("mode", Mode.NORMAL.getCode());

            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("v1-slot-normal-add-user-in-0(2)"));

            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + initiator.tokens[1].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("v1-slot-normal-add-user-in-0(2)"));

           GameDto game = addGame(SlotDto.from(slot));

            mockMvc.perform(get("/pingpong/games/players").contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                    .andExpect(jsonPath("$.matchTeamsInfo.myTeam.teams[0].intraId").value(users[0].getIntraId()))
                    .andExpect(jsonPath("$.matchTeamsInfo.myTeam.teams[0].userImageUri").value(users[0].getImageUri()))
                    .andExpect(jsonPath("$.matchTeamsInfo.enemyTeam.teams[0].intraId").value(users[1].getIntraId()))
                    .andExpect(jsonPath("$.matchTeamsInfo.enemyTeam.teams[0].userImageUri").value(users[1].getImageUri()))
                    .andExpect(status().isOk())
                    .andDo(document("v1-normal-game-find-results-single"));


            mockMvc.perform(post("/pingpong/games/result/normal").contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                    .andExpect(status().isCreated());

            User afterUser = userRepository.findById(user.getId()).get();
            mockMvc.perform(get("/pingpong/games/{gameId}/result/normal", game.getId()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.beforeLevel").value(currentLevel))
                    .andExpect(jsonPath("$.beforeExp").value(currentExp))
                    .andExpect(jsonPath("$.beforeMaxExp").value(ExpLevelCalculator.getLevelMaxExp(currentLevel)))
                    .andExpect(jsonPath("$.increasedLevel").value(ExpLevelCalculator.getLevel(afterUser.getTotalExp()) - currentLevel))
                    .andExpect(jsonPath("$.increasedExp").value(ExpLevelCalculator.getCurrentLevelMyExp(afterUser.getTotalExp()) - currentExp))
                    .andExpect(jsonPath("$.afterMaxExp").value(ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(afterUser.getTotalExp()))))
                    .andDo(document("v1-exp-result"));
            }
    }
}
