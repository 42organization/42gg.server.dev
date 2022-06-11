package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.scheduler.SlotGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private SlotGenerator slotGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    User user;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    List<User> users;

    List<Slot> slotList;

    @BeforeEach
    void init() {
        user = User.builder().intraId("donghyuk").statusMessage("").ppp(1000).build();
        user1 = User.builder().intraId("nheo").statusMessage("").ppp(1000).build();
        user2 = User.builder().intraId("jekim").statusMessage("").ppp(1000).build();
        user3 = User.builder().intraId("jiyun").statusMessage("").ppp(1000).build();
        user4 = User.builder().intraId("wochae").statusMessage("").ppp(1000).build();
        user5 = User.builder().intraId("hakim").statusMessage("").ppp(1000).build();
        users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        for (User u : users) {
            userRepository.save(u);
        }
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        for (int i = 0; i < 18; i++) {
            Team team1 = teamRepository.save(Team.builder().teamPpp(0).headCount(0).score(0).build());
            Team team2 = teamRepository.save(Team.builder().teamPpp(0).headCount(0).score(0).build());
            LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0); // 3시부터 10분 간격으로 18개 슬롯 생성
            slotRepository.save(Slot.builder()
                    .team1(team1)
                    .team2(team2)
                    .tableId(1)
                    .headCount(0)
                    .time(time)
                    .build());
        }
        slotList = slotRepository.findAll();
    }

    @Transactional
    void addUser(Slot slot, Integer headCount, String type, Integer gamePpp) {
        slot.setHeadCount(headCount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
    }

    @Test
    @Transactional
    void slotStatusList() throws Exception {
        User babyUser = userRepository.save(User.builder().intraId("donghyuk").statusMessage("").ppp(100).build());

        // userPpp = 1000;
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
        params.add("type", "single");
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-1-init"));

        SlotFindStatusDto slotFindStatusDto = SlotFindStatusDto.builder()
                .currentTime(LocalDateTime.now())
                .userId(user.getId())
                .type("single").build();


        // [Close] 슬롯1 & 단식 - 유저 2명
        Slot slot = slotList.get(1);
        addUser(slot, 2, "single", 950);

        params = new LinkedMultiValueMap<>();
        params.add("type", "single");
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-2-after-add-2-user"));

        // [Close] 슬롯 2 & 단식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slotList.get(2);

        addUser(slot, 1, "single", 900);

        params = new LinkedMultiValueMap<>();
        params.add("type", "single");
        params.add("userId", babyUser.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-3-after-enter-100p-in-900p"));

        // [Close] 슬롯 3 & 복식 - 유저 4명
        slot = slotList.get(3);
        addUser(slot, 4, "double", 750);

        params = new LinkedMultiValueMap<>();
        params.add("type", "double");
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-4-with-type-double"));

        // [Close] 슬롯 4 & 복식 - 유저(100p) -> 슬롯(900p) 접근
        slot = slotList.get(2);
        addUser(slot, 4, "dobule", 900);

        params = new LinkedMultiValueMap<>();
        params.add("type", "single");
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
        Slot slotA = slotList.get(0);
        body1.put("slotId", slotA.getId().toString());
        body1.put("type", "single");

        Map<String, String> body2 = new HashMap<>();
        body2.put("slotId", slotA.getId().toString());
        body2.put("type", "single");
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
        params.add("type", "single");
        params.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("slot-add-user"));
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
        addUser(slot, 2, "single", 1000);
        Team team1 = slot.getTeam1();
        addUserInTeam(team1, user);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("slotId", slot.getId().toString());
        params.add("pUserId", user.getId().toString());
        mockMvc.perform(delete("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk());
        MultiValueMap<String, String> newParams = new LinkedMultiValueMap<>();
        newParams.add("type", "single");
        newParams.add("userId", user.getId().toString());
        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(newParams))
                .andExpect(status().isOk())
                .andDo(document("remove-user-in-slot"));
    }
}