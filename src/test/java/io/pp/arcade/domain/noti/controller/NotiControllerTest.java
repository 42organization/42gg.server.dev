package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
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

    @Autowired
    TestInitiator initiator;

    Slot slot;
    User user1;
    User user2;
    User user3;
    User user4;
    Team team1;
    Team team2;

    @BeforeEach
    void init() {
        initiator.letsgo();
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];
        slot =initiator.slots[0];

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
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.ANNOUNCE)
                .message("공지사항")
                .isChecked(false)
                .slot(slot)
                .build());
        slot.setType(GameType.SINGLE);
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();
        team1.setUser1(user1);
        team2.setUser1(user2);
    }

    @Test
    @Transactional
    void notiFindByUser() throws Exception {
        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("find-notifications"));
        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                   .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
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
                    .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("after-delete-one-notification"));
    }

    @Test
    @Transactional
    void notiRemoveAll() throws Exception{
        mockMvc.perform(delete("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("delete-all-notification"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("after-delete-all-notification"));
    }
}