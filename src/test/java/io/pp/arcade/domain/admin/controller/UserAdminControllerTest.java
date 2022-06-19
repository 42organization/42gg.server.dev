package io.pp.arcade.domain.admin.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
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
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class UserAdminControllerTest {
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
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PChangeRepository pChangeRepository;

    User user1;
    User user2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        user1 = userRepository.save(User.builder()
                .intraId("hakim")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        user2 = userRepository.save(User.builder()
                .intraId("nheo")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        user3 = userRepository.save(User.builder()
                .intraId("donghyuk")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        user4 = userRepository.save(User.builder()
                .intraId("wochae")
                .statusMessage("")
                .ppp(1)
                .build()
        );
    }

    @Test
    @Transactional
    public void userCreate() throws Exception {
        //given
        Map<String, String> body = new HashMap<>();
        body.put("intraId", "jiyun");
        body.put("statusMessage", "hi");
        body.put("ppp", "400");

        //when
        mockMvc.perform(post("/admin/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andDo(document("admin-user-create"));
    }

    @Test
    @Transactional
    public void userUpdate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("userId", user1.getId().toString());
        body.put("racketType", "SHAKEHAND");
        body.put("statusMessage", "king");
        body.put("ppp", "500");

        mockMvc.perform(put("/admin/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-user-update"));

        User userUpdated = userRepository.findById(user1.getId()).orElseThrow();
        Assertions.assertThat(userUpdated.getStatusMessage()).isEqualTo("king");
        Assertions.assertThat(userUpdated.getRacketType().name()).isEqualTo("SHAKEHAND");
    }

    @Test
    @Transactional
    public void userFind() throws Exception {
        mockMvc.perform(get("/admin/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-user-find-all"));
    }
}

