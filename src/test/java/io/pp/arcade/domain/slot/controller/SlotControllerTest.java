package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.scheduler.SlotGenerator;
import io.pp.arcade.global.type.GameType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RecordApplicationEvents
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    TestInitiator testInitiator;

    List<Slot> slotList;
    Slot[] slots;
    User[] user;
    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user = testInitiator.users;
        slots = testInitiator.slots;
    }

    @Transactional
    void addUser(Slot slot, Integer headCount, GameType type, Integer gamePpp, User user) {
        slot.setHeadCount(headCount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
        if (user != null) {
            currentMatchRepository.save(CurrentMatch.builder().slot(slot).game(null).user(user).matchImminent(false).isMatched(false).build());
        }
    }

    @Test
    @Transactional
    void slotStatusList() throws Exception {
        User babyUser = user[10];

        mockMvc.perform(get("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-1-init"));

        // [Close] 슬롯1 & 단식 - 유저 2명
        Slot slot = slots[1];
        addUser(slot, 2, GameType.SINGLE, 950, null);

        mockMvc.perform(get("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-2-after-add-2-user"));

        // [Close] 슬롯 2 & 단식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slots[2];

        addUser(slot, 1, GameType.SINGLE, 900, null);

        mockMvc.perform(get("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-3-after-enter-100p-in-900p"));

        // [Close] 슬롯 3 & 복식 - 유저 4명
        slot = slots[3];
        addUser(slot, 4, GameType.BUNGLE, 750, null);

        mockMvc.perform(get("/pingpong/match/tables/1/bungle").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-4-with-type-double"));

        // [Close] 슬롯 4 & 복식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slots[4];
        addUser(slot, 3, GameType.BUNGLE, 900, null);

        mockMvc.perform(get("/pingpong/match/tables/1/bungle").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-5-after-enter-100p-in-900p"));

        // [MyTable] 내가 들어간 슬롯
        slot = slots[5];
        addUser(slot, 1, GameType.BUNGLE, 100, babyUser);

        mockMvc.perform(get("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-6-when-i-have-a-slot"));
    }

    @Test
    @Transactional
    void slotAddUser() throws Exception {
        Map<String, String> body1 = new HashMap<>();
        Slot slotA = slots[0];
        slotA.setType(GameType.SINGLE);
        body1.put("slotId", slotA.getId().toString());
        Map<String, String> body2 = new HashMap<>();
        body2.put("slotId", slotA.getId().toString());
        mockMvc.perform(post("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body2))
                        .header("Authorization", "Bearer 5"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .header("Authorization", "Bearer 6"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/pingpong/match/tables/1/" + GameType.SINGLE).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("slot-add-user"));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("current-match-after-add-match"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("after-matched-notification"));

        mockMvc.perform(delete("/pingpong/match").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 5"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("after-canceled-notification"));
    }

    @Transactional
    void addUserInTeam(Team team, User user) {
        team.setUser1(user);
    }

    @Test
    @Transactional
    void slotRemoveUser() throws Exception {
        // 슬롯에 유저 두 명 추가했다가, 한 명 제거하기
        Slot slot = slots[3];
        addUser(slot, 2, GameType.SINGLE, 1000, user[2]);
        Team team1 = slot.getTeam1();
        addUserInTeam(team1, user[2]);
        mockMvc.perform(delete("/pingpong/match").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/match/tables/1/" + slot.getType().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("remove-user-in-slot"));
    }
}