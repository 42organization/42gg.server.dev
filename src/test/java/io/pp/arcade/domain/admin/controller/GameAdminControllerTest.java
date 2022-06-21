package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.season.Season;
import io.pp.arcade.domain.season.SeasonRepository;
import io.pp.arcade.domain.slot.Slot;
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
public class GameAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SeasonRepository seasonRepository;

    User user1;
    User user2;
    User user3;
    User user4;
    Team team1;
    Team team2;
    Team team3;
    Team team4;
    Slot slot1;
    Slot slot2;
    Game game1;
    Season season;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];
        user3 = testInitiator.users[2];
        user4 = testInitiator.users[3];
        team1 = testInitiator.teams[0];
        team2 = testInitiator.teams[1];
        team3 = testInitiator.teams[2];
        team4 = testInitiator.teams[3];
        slot1 = testInitiator.slots[0];
        slot1.setType(GameType.SINGLE);
        slot2 = testInitiator.slots[1];
        slot2.setType(GameType.SINGLE);
        season = seasonRepository.save(Season.builder()
                .seasonName("7기")
                .startTime(LocalDateTime.of(2022, 7, 4, 15, 00))
                .endTime(LocalDateTime.of(2023, 1, 4, 15, 00))
                .startPpp(100)
                .pppGap(50)
                .build());

        game1 = gameRepository.save(Game.builder()
                .slot(slot1)
                .team1(slot1.getTeam1())
                .team2(slot1.getTeam2())
                .type(slot1.getType())
                .time(slot1.getTime())
                .season(season.getId())
                .status(StatusType.WAIT)
                .build());

    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("slotId", slot2.getId().toString());
        body.put("seasonId", season.getId().toString());
        body.put("status", StatusType.END.toString());

        mockMvc.perform(post("/admin/game").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-game-create"));

        Game createdGame = gameRepository.findBySlot(slot2).orElseThrow(null);
        Assertions.assertThat(createdGame.getType()).isEqualTo(slot2.getType());
        Assertions.assertThat(createdGame.getStatus()).isEqualTo(StatusType.END);
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("gameId", game1.getId().toString());
        body.put("slotId", slot1.getId().toString());
        body.put("team1Id", slot1.getTeam1().getId().toString());
        body.put("team2Id", slot1.getTeam2().getId().toString());
        body.put("seasonId", season.getId().toString());
        body.put("time", LocalDateTime.of(2022, 7, 6, 15, 20).toString());
        body.put("status", StatusType.LIVE.toString());

        mockMvc.perform(put("/admin/game").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-game-update"));

        Game updatedGame = gameRepository.findBySlot(slot1).orElseThrow(null);
        Assertions.assertThat(updatedGame.getTime()).isEqualTo(LocalDateTime.of(2022, 7, 6, 15, 20));
        Assertions.assertThat(updatedGame.getType()).isEqualTo(StatusType.LIVE);
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        mockMvc.perform(delete("/admin/game/" + game1.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-game-delete"));
        int size = gameRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(0);
    }

    @Test
    @Transactional
    public void findGame() throws Exception {
        mockMvc.perform(get("/admin/game").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-game-find-all"));

        int size = gameRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }
}
