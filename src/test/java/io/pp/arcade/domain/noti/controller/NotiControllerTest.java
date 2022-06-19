package io.pp.arcade.domain.noti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.noti.NotiRepository;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.NotiType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class NotiControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    NotiRepository notiRepository;

    @Autowired
    NotiService notiService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SlotRepository slotRepository;

    Slot slot;
    Team team1;
    User user1;
    User user2;
    Team team2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        user1 = userRepository.save(User.builder().intraId("jiyun1").statusMessage("").ppp(42).build());
        user2 = userRepository.save(User.builder().intraId("jiyun2").statusMessage("").ppp(24).build());
        user3 = userRepository.save(User.builder().intraId("nheo1").statusMessage("").ppp(60).build());
        user4 = userRepository.save(User.builder().intraId("nheo2").statusMessage("").ppp(30).build());
        team1 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .user1(user1)
                .user2(user2)
                .headCount(2)
                .score(0)
                .build());
        team2 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .user1(user3)
                .user2(user4)
                .headCount(2)
                .score(0)
                .build());
        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .type(GameType.DOUBLE)
                .time(LocalDateTime.now())
                .headCount(4)
                .build());

        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
                .isChecked(false)
                .slot(slot)
                .build());
    }

    @Test
    @Transactional
    void notiFindByUser() throws Exception {
        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("find-notifications"));
        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("find-notifications-twice"));
    }

    @Test
    @Transactional
    void notiRemoveOne() throws Exception{
        mockMvc.perform(delete("/pingpong/notifications/" + notiRepository.findAll().get(0).getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("delete-one-notification"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("after-delete-one-notification"));
    }

    @Test
    @Transactional
    void notiRemoveAll() throws Exception{
        mockMvc.perform(delete("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("delete-all-notification"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("after-delete-all-notification"));
    }
}