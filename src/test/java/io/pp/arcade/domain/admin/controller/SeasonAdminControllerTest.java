package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.season.Season;
import io.pp.arcade.domain.season.SeasonRepository;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class SeasonAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SeasonRepository seasonRepository;

    Season season;

    @BeforeEach
    void init() {
        season = Season.builder()
                .seasonName("7기")
                .startTime(LocalDateTime.of(2022, 7, 4, 15, 00))
                .endTime(LocalDateTime.of(2023, 1, 4, 15, 00))
                .startPpp(100)
                .pppGap(50)
                .build();
        seasonRepository.save(season);
    }

    @Test
    @Transactional
    public void seasonCreate() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 5, 15, 00);
        LocalDateTime endTime = LocalDateTime.of(2023, 7, 5, 15, 00);

        Map<String, String> body = new HashMap<>();
        body.put("seasonName", "8기");
        body.put("startTime", startTime.toString());
        body.put("endTime", endTime.toString());
        body.put("startPpp", "200");
        body.put("pppGap", "50");

        mockMvc.perform(post("/admin/season").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-season-create"));
    }

    @Test
    @Transactional
    public void seasonDelete() throws Exception {
        mockMvc.perform(delete("/admin/season/" + season.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-season-delete"));
    }

    @Test
    @Transactional
    public void seasonFind() throws Exception {
        mockMvc.perform(get("/admin/season").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-season-find-all"));
    }
}
