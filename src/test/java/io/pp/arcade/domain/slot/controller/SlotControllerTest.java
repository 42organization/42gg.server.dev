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
    private SlotRepository slotRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    TestInitiator testInitiator;

    User user;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;

    List<Slot> slotList;


    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user = userRepository.findByIntraId("donghyuk").orElse(null);
        user1 = userRepository.findByIntraId("nheo").orElse(null);
        user2 = userRepository.findByIntraId("jekim").orElse(null);
        user3 = userRepository.findByIntraId("jiyun").orElse(null);
        user4 = userRepository.findByIntraId("wochae").orElse(null);
        user5 = userRepository.findByIntraId("hakim").orElse(null);
        slotList = slotRepository.findAll();
    }

    @Transactional
    void addUser(Slot slot, Integer headCount, GameType type, Integer gamePpp, User user) {
        slot.setHeadCount(headCount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
        currentMatchRepository.save(CurrentMatch.builder().slot(slot).game(null).user(user).matchImminent(false).isMatched(false).build());
    }

    @Test
    @Transactional
    void slotStatusList() throws Exception {
        User babyUser = userRepository.save(User.builder().intraId("donghyuk").statusMessage("").ppp(100).build());

        // userPpp = 1000;
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.SINGLE.toString());
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-1-init"));

        SlotFindStatusDto slotFindStatusDto = SlotFindStatusDto.builder()
                .currentTime(LocalDateTime.now())
                .userId(user.getId())
                .type(GameType.SINGLE).build();


        // [Close] 슬롯1 & 단식 - 유저 2명
        Slot slot = slotList.get(1);
        addUser(slot, 2, GameType.SINGLE, 950, user);

        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.SINGLE.toString());
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-2-after-add-2-user"));

        // [Close] 슬롯 2 & 단식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slotList.get(2);

        addUser(slot, 1, GameType.SINGLE, 900, babyUser);

        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.SINGLE.toString());
        params.add("userId", babyUser.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-3-after-enter-100p-in-900p"));

        // [Close] 슬롯 3 & 복식 - 유저 4명
        slot = slotList.get(3);
        addUser(slot, 4, GameType.DOUBLE, 750, user);

        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.DOUBLE.toString());
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-4-with-type-double"));

        // [Close] 슬롯 4 & 복식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slotList.get(2);
        addUser(slot, 4, GameType.DOUBLE, 900, babyUser);

        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.SINGLE.toString());
        params.add("userId", babyUser.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-5-after-enter-100p-in-900p"));
    }

    @Test
    @Transactional
    void slotAddUser() throws Exception {
        Map<String, String> body1 = new HashMap<>();
        Slot slotA = slotList.get(1);
        body1.put("slotId", slotA.getId().toString());
        body1.put("type", GameType.SINGLE.toString());

        Map<String, String> body2 = new HashMap<>();
        body2.put("slotId", slotA.getId().toString());
        body2.put("type", GameType.SINGLE.toString());
        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .param("userId", user4.getId().toString()))
                .andExpect(status().isOk());
        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body2))
                        .param("userId", user5.getId().toString()))
                .andExpect(status().isOk());
        try {
            mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body1))
                            .param("userId", user3.getId().toString()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.err.println("유저가 3명이라 터졌을지도!");
        }
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
        params.add("type", GameType.SINGLE.toString());
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-add-user"));


        mockMvc.perform(RestDocumentationRequestBuilders.get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId",user4.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-after-add-match"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user4.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("after-matched-notification"));

        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();

        params1.add("slotId", slotA.getId().toString());
        params1.add("pUserId", user5.getId().toString());
        mockMvc.perform(delete("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params1))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user4.getId().toString()))
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
        Slot slot = slotList.get(3);
        addUser(slot, 2, GameType.SINGLE, 1000, user);
        Team team1 = slot.getTeam1();
        addUserInTeam(team1, user);
        mockMvc.perform(delete("/pingpong/match").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/match/tables/1/" + slot.getType().toString()).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("remove-user-in-slot"));
    }
}