package io.pp.arcade.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.noti.NotiRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
class NotiControllerTest {
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
    NotiRepository notiRepository;

    User notiUser;

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        Season[] seasons = realWorld.makeDefaultSeasons();
        realWorld.makeMixedGameResultsForDayAmongPastSeason(seasons[0], 120);
        notiUser = realWorld.getUserWithVariousNotis();
    }

    @Test
    @Order(1)
    @Transactional
    void notiFindByUser() throws Exception {
        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(notiUser).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/NotiControllers/GET/notifications (알림 조회)",
                        relaxedResponseFields(
                                fieldWithPath("notifications").description("list of notifications"),
                                fieldWithPath("notifications[].id").description("id of notification"),
                                fieldWithPath("notifications[].type").description("type of notification(ANNOUNCE, MATCHED, IMMINENT, CANCELEDBYMAN, CANCELEDBYTIME"),
                                fieldWithPath("notifications[].isChecked").description("whether the noti is checked"),
                                fieldWithPath("notifications[].createdAt").description("time of noti's creation"),
                                fieldWithPath("notifications[].message").description("notification's message(OPTIONAL, only for ANNOUNCE type)").optional(),
                                fieldWithPath("notifications[].time").description("time of noti's slot(OPTIONAL, for MATCHED, IMMINENT, CANCELEDBYMAN, CANCELEDBYTIME type)").optional(),
                                fieldWithPath("notifications[].myTeam").description("myTeam user Id of noti's slot(OPTIONAL, only for IMMINENT type)").optional(),
                                fieldWithPath("notifications[].enemyTeam").description("enemyTeam user Id of noti's slot(OPTIONAL, only for IMMINENT type)").optional()
                        ))
                );

    }

    @Test
    @Order(2)
    @Transactional
    void notiRemoveOne() throws Exception {
        mockMvc.perform(delete("/pingpong/notifications/{notiId}", notiRepository.findAll().get(0).getId().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(notiUser).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/NotiControllers/DELETE/notifications-{notiId} (개별 알림 삭제)",
                        pathParameters(
                                parameterWithName("notiId").description("id of noti to delete")
                        ))
                );
    }

    @Test
    @Order(3)
    @Transactional
    void notiRemoveAll() throws Exception {
        mockMvc.perform(delete("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(notiUser).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/NotiControllers/DELETE/notifications (모든 알림 삭제)"));
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}