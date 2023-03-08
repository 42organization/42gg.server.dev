package io.pp.arcade.v1.admin.announcement.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import io.pp.arcade.v1.domain.announcement.Announcement;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AnnouncementAdminControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AnnouncementAdminRepository announcementAdminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    private List<User> users;
    private List<Token> tokens;
    @BeforeEach
    void init() {
        databaseCleanup.execute();
        initUsers();
        initTokens();
        Announcement announcement1 = Announcement.builder()
                .content("test1: we need to change announcement!")
                .createdTime(LocalDateTime.now().minusDays(2))
                .creatorIntraId("yuikim")
                .build();
        announcementAdminRepository.save(announcement1);
        announcement1.update("euikim", LocalDateTime.now().minusDays(1));

        Announcement announcement2 = Announcement.builder()
                .content("test2: we need to change announcement!")
                .createdTime(LocalDateTime.now().minusHours(1))
                .creatorIntraId("rjeong")
                .build();
        announcementAdminRepository.save(announcement2);
        announcement2.update("yuikim", LocalDateTime.now());
    }
    @Test
    @DisplayName("관리자가 공지사항 관리자 페이지 조회")
    @Transactional //테스트 완료 후 roll back
    void announcementListByAdmin() throws Exception{
        mockMvc.perform(get("/pingpong/admin/announcement").contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokens.get(0).getAccessToken()))
                .andExpect(jsonPath("$.announcementList[1].content").value("test1: we need to change announcement!"))
                .andExpect(jsonPath("$.announcementList[1].creatorIntraId").value("yuikim"))
                .andExpect(jsonPath("$.announcementList[1].deleterIntraId").value("euikim"))
                .andExpect(jsonPath("$.announcementList[0].content").value("test2: we need to change announcement!"))
                .andExpect(jsonPath("$.announcementList[0].creatorIntraId").value("rjeong"))
                .andExpect(jsonPath("$.announcementList[0].deleterIntraId").value("yuikim"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일반 사용자가 공지사항 관리자 페이지 조회")
    @Transactional
    void announcementListByUser() throws Exception{
        mockMvc.perform(get("/pingpong/admin/announcement").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokens.get(1).getAccessToken()))
                .andExpect(status().isForbidden());

    }
    @Test
    @DisplayName("최근 공지사항이 삭제된 후 관리자가 공지사항 생성")
    @Transactional
    void announcementAddByAdminAfterCurrentAnnouncementIsDel() throws Exception{
        Map<String, String> info = new HashMap<>();
        info.put("creatorIntraId", "yuikim");
        info.put("createdTime", "2023-03-06T20:19:37");
        info.put("content", "<p><strong class=\"ql-size-huge\">******공지사항******</strong></p>");
        mockMvc.perform(post("/pingpong/admin/announcement")
                    .content(objectMapper.writeValueAsString(info))
                .header("Authorization", "Bearer " + tokens.get(0).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("최근 공지상항이 삭제 되지 않은 상태에서 관리자가 공지사항 생성")
    void announcementAddByAdminBeforeCurrentAnnouncementIsDel() throws Exception{
        Announcement announcement3 = Announcement.builder()
                .content("test3: we need to change announcement!")
                .createdTime(LocalDateTime.now().minusDays(2))
                .creatorIntraId("admin3")
                .build();
        announcementAdminRepository.save(announcement3);
        Map<String, String> info = new HashMap<>();
        info.put("creatorIntraId", "yuikim");
        info.put("createdTime", "2023-03-06T20:19:37");
        info.put("content", "<p><strong class=\"ql-size-huge\">******공지사항******</strong></p>");
        mockMvc.perform(post("/pingpong/admin/announcement")
                        .content(objectMapper.writeValueAsString(info))
                        .header("Authorization", "Bearer " + tokens.get(0).getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("최근 공지사항이 삭제된 상태에서 공지사항 삭제")
    void announcementModify() throws Exception{
        Map<String, String> info = new HashMap<>();
        info.put("deleterIntraId", "yuikim");
        info.put("deletedTime", "2023-03-06T20:19:37");
        mockMvc.perform(put("/pingpong/admin/announcement")
                .content(objectMapper.writeValueAsString(info))
                .header("Authorization", "Bearer " + tokens.get(0).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    private void initUsers() {
        users = new ArrayList<User>();

        User user1 = userRepository.save(User.builder().intraId("yuikim")
                .eMail("yuikim@student42.seoul.kr")
                .imageUri("null")
                .statusMessage("test1")
                .ppp(1000)
                .roleType(RoleType.ADMIN)
                .racketType(RacketType.DUAL)
                .totalExp(400)
                .build());
        User user2 = userRepository.save(User.builder().intraId("rolee")
                .eMail("rolee@student42.seoul.kr")
                .imageUri("null")
                .statusMessage("test2")
                .ppp(1000)
                .roleType(RoleType.USER)
                .racketType(RacketType.SHAKEHAND)
                .totalExp(400)
                .build());
        users.add(user1);
        users.add(user2);
    }
    private void initTokens() {
        tokens = new ArrayList<Token>();
        for (Integer i = 0; i < 2; i++) {
            Token token = tokenRepository.save(new Token(users.get(i), i.toString(), i.toString()));
            tokens.add(token);
        }
    }
}
