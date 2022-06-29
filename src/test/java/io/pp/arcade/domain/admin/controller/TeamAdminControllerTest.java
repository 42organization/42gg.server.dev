package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.team.Team;
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
public class TeamAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    User user1;
    User user2;

    Team team1;
    Team team2;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];

        team1 = testInitiator.teams[0];
        team2 = testInitiator.teams[1];
    }

    @Test
    @Transactional
    public void teamCreate() throws Exception {
        //given
        Map<String, String> body = new HashMap<>();
        body.put("teamPpp", "0");
        body.put("headCount", "0");
        body.put("score", "0");
        //then
        mockMvc.perform(post("/admin/team").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-team-create"));
    }

    @Test
    @Transactional
    public void teamUpdate() throws Exception {
        //given
        Map<String, String> body = new HashMap<>();
        body.put("teamId", team1.getId().toString());
        body.put("user1Id", user1.getId().toString());
        body.put("user2Id", "3");
        body.put("teamPpp", "10");
        body.put("headCount", "1");
        body.put("score", "30");
        //then
        mockMvc.perform(put("/admin/team").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-team-update"));

        Team teamUpdated = teamRepository.findById(team1.getId()).orElseThrow(null);
        Assertions.assertThat(teamUpdated.getTeamPpp()).isEqualTo(10);
    }

    @Test
    @Transactional
    public void teamDelete() throws Exception {
        mockMvc.perform(delete("/admin/team/" + team2.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-team-delete"));
    }

    @Test
    @Transactional
    public void teamFind() throws Exception {
        mockMvc.perform(get("/admin/team/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-team-find-all"));
    }
}
