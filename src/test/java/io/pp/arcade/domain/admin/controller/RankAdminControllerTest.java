package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.rank.RankRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.RacketType;
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
public class RankAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    TestInitiator testInitiator;

    User user1;
    User user2;
    Rank rank;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];

        rank = rankRepository.save(Rank.builder()
                .user(user1)
                .seasonId(1)
                .ranking(5)
                .racketType(RacketType.SHAKEHAND)
                .ppp(300)
                .wins(5)
                .losses(1)
                .build()
        );
    }

    @Test
    @Transactional
    public void rankCreate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("userId", user2.getId().toString());
        body.put("seasonId", "1");
        body.put("ranking", "4");
        body.put("ppp", "400");
        body.put("racketType", RacketType.PENHOLDER.toString());
        body.put("wins", "6");
        body.put("losses", "1");

        mockMvc.perform(post("/admin/rank").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-rank-create"));

        int size = rankRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(2);
    }

    @Test
    @Transactional
    public void rankUpdate() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("rankId", rank.getId().toString());
        body.put("ppp", "250");
        body.put("losses", "2");

        mockMvc.perform(put("/admin/rank").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-rank-update"));

        Assertions.assertThat(rank.getPpp()).isEqualTo(250);
        Assertions.assertThat(rank.getLosses()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void rankDelete() throws Exception {
        mockMvc.perform(delete("/admin/rank/" + rank.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-rank-delete"));

        int size = rankRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(0);
    }

    @Test
    @Transactional
    public void rankFind() throws Exception {
        mockMvc.perform(get("/admin/rank").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-rank-find-all"));

        int size = rankRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }
}
