package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.noti.NotiRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.NotiType;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class NotiAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private NotiRepository notiRepository;

    User user1;
    User user2;
    Team team1;
    Team team2;
    Slot slot1;
    Slot slot2;

    Noti noti1;
    Noti noti2;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];
        team1 = testInitiator.teams[0];
        team2 = testInitiator.teams[1];
        slot1 = testInitiator.slots[0];
        slot2 = testInitiator.slots[1];

        noti1 = notiRepository.save(Noti.builder()
                .user(user1)
                .slot(slot1)
                .type(NotiType.IMMINENT)
                .isChecked(false)
                .message("Get ready!")
                .build());

        noti2 = notiRepository.save(Noti.builder()
                .user(user1)
                .slot(slot1)
                .type(NotiType.MATCHED)
                .isChecked(true)
                .message("Matching done!")
                .build());
    }

    @Test
    @Transactional
    public void notiCreate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("userId", user1.getId().toString());
        body.put("slotId", slot1.getId().toString());
        body.put("notiType", NotiType.ANNOUNCE.toString());
        body.put("isChecked", "false");
        body.put("message", "welcome");

        mockMvc.perform(post("/admin/noti").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-noti-create"));

        List<Noti> notis = notiRepository.findAllByUser(user1);
        int count = 0;
        for (Noti noti: notis) {
            Assertions.assertThat(noti.getSlot().getId()).isEqualTo(slot1.getId());
            count += 1;
        }
        Assertions.assertThat(count).isEqualTo(3);
    }

    @Test
    @Transactional
    public void updateNoti() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("notiId", noti2.getId().toString());
        body.put("userId", user1.getId().toString());
        body.put("slotId", slot1.getId().toString());
        body.put("notiType", NotiType.IMMINENT.toString());
        body.put("isChecked", "false");
        body.put("message", "Get ready!");

        mockMvc.perform(put("/admin/noti").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-noti-update"));

        Noti updatedNoti = notiRepository.findById(noti2.getId()).orElseThrow();
        Assertions.assertThat(updatedNoti.getMessage()).isEqualTo("Get ready!");
        Assertions.assertThat(updatedNoti.getType()).isEqualTo(NotiType.IMMINENT);
        Assertions.assertThat(updatedNoti.getIsChecked()).isEqualTo(false);
    }

    @Test
    @Transactional
    public void deleteNoti() throws Exception {
        mockMvc.perform(delete("/admin/noti/" + noti1.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-noti-delete"));

        int size = notiRepository.findAllByUser(user1).size();
        Assertions.assertThat(size).isEqualTo(1);
    }

    @Test
    @Transactional
    public void findNoti() throws Exception {
        mockMvc.perform(get("/admin/noti/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-noti-find-all"));

        int size = notiRepository.findAllByUser(user1).size();
        Assertions.assertThat(size).isEqualTo(2);
    }
}
