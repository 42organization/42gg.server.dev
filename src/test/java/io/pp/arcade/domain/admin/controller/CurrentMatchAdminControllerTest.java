package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class CurrentMatchAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private GameRepository gameRepository;

    User userInTeam1;
    User userInTeam3;
    User user3;

    Team team1InSlot2;
    Team team2InSlot2;
    Team team3InSlot1;
    Team team4InSlot1;
    Team team5;
    Team team6;

    Slot slot1InGame1;
    Slot slot2InGame2;
    Slot slot3;

    Game game1;
    Game game2;
    Game game3;

    CurrentMatch currentMatch;
    CurrentMatch currentMatch1WithUserInTeam3;
    CurrentMatch currentMatch2WithUserInTeam3;

    @BeforeEach
    void init() {
        testInitiator.letsgo();

        userInTeam3 = testInitiator.users[4];
        team3InSlot1 = testInitiator.teams[4];
        team4InSlot1 = testInitiator.teams[5];

        slot1InGame1 = slotRepository.save(Slot.builder()
                .team1(team3InSlot1)
                .team2(team4InSlot1)
                .tableId(1)
                .type(GameType.DOUBLE)
                        .headCount(4)
                .time(LocalDateTime.of(2022, 6, 22, 14,00))
                .build());

        // 복식
        game1 = gameRepository.save(Game.builder()
                .slot(slot1InGame1)
                .team1(slot1InGame1.getTeam1())
                .team2(slot1InGame1.getTeam2())
                .type(slot1InGame1.getType())
                .time(slot1InGame1.getTime())
                .season(1)
                .status(StatusType.WAIT)
                .build());

        userInTeam1 = testInitiator.users[0];
        team1InSlot2 = testInitiator.teams[0];
        team2InSlot2 = testInitiator.teams[1];

        slot2InGame2 = slotRepository.save(Slot.builder()
                .team1(team1InSlot2)
                .team2(team2InSlot2)
                .tableId(1)
                        .headCount(2)
                .type(GameType.SINGLE)
                .time(LocalDateTime.of(2022, 6, 22, 14,20))
                .build());

        // 단식
        game2 = gameRepository.save(Game.builder()
                .slot(slot2InGame2)
                .team1(slot2InGame2.getTeam1())
                .team2(slot2InGame2.getTeam2())
                .type(slot2InGame2.getType())
                .time(slot2InGame2.getTime())
                .season(1)
                .status(StatusType.WAIT)
                .build());

        /* update, delete test를 위한, 5분 전 아직 아님 */
        currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .user(userInTeam1)
                .slot(slot2InGame2)
                .game(game2)
                .matchImminent(false)
                .isMatched(false)
                .build());

        user3 = testInitiator.users[2];
        team5 = testInitiator.teams[2];
        team6 = testInitiator.teams[3];
        slot3 = slotRepository.save(Slot.builder()
                .team1(team5)
                .team2(team6)
                .tableId(1)
                        .headCount(2)
                .type(GameType.SINGLE)
                .time(LocalDateTime.of(2022, 6, 22, 14,30))
                .build());

        game3 = gameRepository.save(Game.builder()
                .slot(slot3)
                .team1(slot3.getTeam1())
                .team2(slot3.getTeam2())
                .type(slot3.getType())
                .time(slot3.getTime())
                .season(1)
                .status(StatusType.WAIT)
                .build());


        /* findall test를 위한, 같은 유저 다른 상태 */
        currentMatch1WithUserInTeam3 = currentMatchRepository.save(CurrentMatch.builder()
                .user(user3)
                .slot(slot3)
                .game(game3)
                .matchImminent(false)
                .isMatched(false)
                .build());
        currentMatch2WithUserInTeam3 = currentMatchRepository.save(CurrentMatch.builder()
                .user(user3)
                .slot(slot3)
                .game(game3)
                .matchImminent(true)
                .isMatched(false)
                .build());
    }

    @Test
    @Transactional
    public void createCurrentMatch() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("userId", userInTeam3.getId().toString());
        body.put("slotId", slot1InGame1.getId().toString());
        body.put("gameId", game1.getId().toString());
        body.put("matchImminent", "false");
        body.put("isMatched", "false");

        mockMvc.perform(post("/admin/currentMatch").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-currentMatch-create"));

        CurrentMatch createdCurrentMatch = currentMatchRepository.findByUser(userInTeam3).orElseThrow(null);
        Assertions.assertThat(createdCurrentMatch.getSlot().getType()).isEqualTo(GameType.DOUBLE);
    }

    @Test
    @Transactional
    public void updateCurrentMatch() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("currentMatchId", currentMatch.getId().toString());
        body.put("userId", currentMatch.getUser().getId().toString());
        body.put("slotId", currentMatch.getSlot().getId().toString());
        body.put("gameId", currentMatch.getGame().getId().toString());
        body.put("matchImminent", "true");
        body.put("isMatched", "true");

        mockMvc.perform(put("/admin/currentMatch").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-currentMatch-update"));

        CurrentMatch updatedCurrentMatch = currentMatchRepository.findById(currentMatch.getId()).orElseThrow(null);
        Assertions.assertThat(updatedCurrentMatch.getMatchImminent()).isEqualTo(true);
        Assertions.assertThat(updatedCurrentMatch.getIsMatched()).isEqualTo(true);
    }

    @Test
    @Transactional
    public void deleteCurrentMatch() throws Exception {
        mockMvc.perform(delete("/admin/currentMatch/" + currentMatch.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-currentMatch-delete"));

        int size = currentMatchRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(2);
    }

    @Test
    @Transactional
    public void findCurrentMatch() throws Exception {
        mockMvc.perform(get("/admin/currentMatch").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-currentMatch-fidn-all"));

        int size = currentMatchRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(3);
    }
}
