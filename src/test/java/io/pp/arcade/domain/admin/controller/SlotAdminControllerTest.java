package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    Team team3;
    Slot slot;

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
                .intraId("woche")
                .statusMessage("")
                .ppp(1)
                .build()
        );

        team1 = teamRepository.save(Team.builder().teamPpp(0)
                .user1(user1).headCount(1).score(0).build());
        team2 = teamRepository.save(Team.builder().teamPpp(0)
                .user1(user2).headCount(1).score(0).build());
        team3 = teamRepository.save(Team.builder().teamPpp(0)
                .user1(user3).headCount(1).score(0).build());

        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .time(LocalDateTime.of(2022,6,18,15,10))
                .headCount(2)
                .gamePpp(0)
                .build()
        );
    }

    @Test
    @Transactional
    public void slotCreate() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> body = new HashMap<>();
        body.put("tableId", "1");
        body.put("time", LocalDateTime.now().toString());
        body.put("headCount", "0");
        body.put("type", "single");

        mockMvc.perform(post("/admin/slot").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-slot-create"));

        Slot slot = slotRepository.findByTime(now).orElseThrow();
        Assertions.assertThat(slot.getType()).isEqualTo("single");
    }

    @Test
    @Transactional
    public void slotUpdate() throws Exception {
    }
}
