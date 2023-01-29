package io.pp.arcade.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.announcement.Announcement;
import io.pp.arcade.v1.domain.announcement.AnnouncementRepository;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.oauth.v2.repository.UserRefreshTokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
public class AnnouncementController {
    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RealWorld realWorld;
    @Autowired
    UserRefreshTokenRepository tokenRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    AnnouncementRepository announcementRepository;

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        realWorld.makeDefaultSeasons();
        announcementRepository.save(Announcement.builder()
                .title("공지사항 제목")
                .content("내용")
                .isDel(false)
                .link("link")
                .build());
    }

    @Test
    @Order(1)
    @Transactional
    void announcementFind() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/announcements")
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/Announcement/getAnnouncement",
                        relaxedResponseFields(
                                fieldWithPath("announcements").description("list of announcements"),
                                fieldWithPath("announcements[].title").description("announcement title"),
                                fieldWithPath("announcements[].content").description("announcement content"),
                                fieldWithPath("announcements[].link").description("link for detail announcement")
                        )
                ));
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}
