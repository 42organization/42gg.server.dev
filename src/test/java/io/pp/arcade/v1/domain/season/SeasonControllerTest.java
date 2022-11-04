package io.pp.arcade.v1.domain.season;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class SeasonControllerTest {

    @Autowired
    private TestInitiator testInitiator;
    @Autowired
    private MockMvc mockMvc;

    Slot[] slots;
    User[] users;
    Team[] teams;
    Season[] seasons;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        teams = testInitiator.teams;
        users = testInitiator.users;
        slots = testInitiator.slots;
        seasons = testInitiator.pastSeasons;
    }

    @Test
    @Transactional
    void getSeasonList() throws Exception {
        mockMvc.perform(get("/pingpong/seasonlist").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.seasonList.length()").value(5))
                .andExpect(status().isOk())
                .andDo(document("seasonList"));
    }
}