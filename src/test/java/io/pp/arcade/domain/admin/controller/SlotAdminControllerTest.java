package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
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
public class SlotAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private SlotRepository slotRepository;

    User user1;
    User user2;
    User user3;
    Team team1;
    Team team2;

    Slot slot;

    @Autowired
    TestInitiator testInitiator;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];
        user3 = testInitiator.users[2];

        team1 = testInitiator.teams[0];
        team2 = testInitiator.teams[1];
        slot = testInitiator.slots[0];
    }

    @Test
    @Transactional
    public void slotCreate() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        Map<String, String> body = new HashMap<>();
        body.put("tableId", "1");
        body.put("team1Id", team1.getId().toString());
        body.put("team2Id", team2.getId().toString());
        body.put("time", time.toString());
        body.put("gamePpp", "0");
        body.put("headCount", "0");
        body.put("type", GameType.SINGLE.toString());

        mockMvc.perform(post("/admin/slot").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-slot-create"));

        Slot createdSlot = slotRepository.findByTime(time).orElseThrow(null);
        Assertions.assertThat(createdSlot.getType()).isEqualTo(GameType.SINGLE);
    }

    @Test
    @Transactional
    public void slotUpdate() throws Exception {

        Map<String, String> body = new HashMap<>();
        body.put("slotId", slot.getId().toString());
        body.put("gamePpp", "100");

        mockMvc.perform(put("/admin/slot").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-slot-update"));

        Slot updatedSlot = slotRepository.findById(slot.getId()).orElseThrow();
        Assertions.assertThat(updatedSlot.getGamePpp()).isEqualTo(100);
    }

    @Test
    @Transactional
    public void slotDelete() throws Exception {
        mockMvc.perform(delete("/admin/slot/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-slot-delete"));
    }

    @Test
    @Transactional
    public void slotFind() throws Exception {
        mockMvc.perform(get("/admin/slot").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-slot-find-all"));
    }
 }
