package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.season.Season;
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
public class PChangeAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private PChangeRepository pChangeRepository;
    @Autowired
    private GameRepository gameRepository;


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
    Game game2;

    PChange pChange;

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
        slot2 = testInitiator.slots[1];

        game1 = gameRepository.save(Game.builder()
                .slot(slot1)
                .team1(team1)
                .team2(team2)
                .time(slot1.getTime())
                .season(1)
                .status(StatusType.END)
                .type(GameType.SINGLE)
                .build());

        game2 = gameRepository.save(Game.builder()
                .slot(slot2)
                .team1(team3)
                .team2(team4)
                .time(slot2.getTime())
                .season(1)
                .status(StatusType.END)
                .type(GameType.SINGLE)
                .build());

        pChange = pChangeRepository.save(PChange.builder()
                .game(game2)
                .user(user3)
                .pppChange(30)
                .pppResult(30)
                .build());
    }

    @Test
    @Transactional
    public void pChangeCreate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("gameId", game1.getId().toString());
        body.put("userId", user1.getId().toString());
        body.put("pppChange", "50");
        body.put("pppResult", "50");

        mockMvc.perform(post("/admin/pChange").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-pChange-create"));

        PChange createdPChange = pChangeRepository.findByUserAndGame(user1, game1).orElseThrow(null);
        Assertions.assertThat(createdPChange.getPppChange()).isEqualTo(50);
        Assertions.assertThat(createdPChange.getPppResult()).isEqualTo(50);
    }

    @Test
    @Transactional
    public void pChangeUpdate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("pchangeId", pChange.getId().toString()); //uri 다 소문자
        body.put("gameId", game2.getId().toString());
        body.put("userId", user3.getId().toString());
        body.put("pppChange", "40");
        body.put("pppResult", "90");

        mockMvc.perform(put("/admin/pChange").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-pChange-update"));

        PChange updatedPChange = pChangeRepository.findById(pChange.getId()).orElseThrow(null);
        Assertions.assertThat(updatedPChange.getPppResult()).isEqualTo(90);
    }

    @Test
    @Transactional
    public void pChangeDelete() throws Exception {
        mockMvc.perform(delete("/admin/pChange/" + pChange.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-pChange-delete"));

        int size = pChangeRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(0);
    }

    @Test
    @Transactional
    public void pChangeFind() throws Exception {
        mockMvc.perform(get("/admin/pChange/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-pChange-find-all"));

        int size = pChangeRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }
}
