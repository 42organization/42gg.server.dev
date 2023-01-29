package io.pp.arcade.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.oauth.v2.repository.UserRefreshTokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.RacketType;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
class UserControllerTest {
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

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        Season[] seasons = realWorld.makeDefaultSeasons();
        realWorld.makeMixedGameResultsForDayAmongPastSeason(seasons[0], 120);
    }

    @Test
    @Order(1)
    @Transactional
    void userFind() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/users").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users (유저 기본정보 조회)",
                        relaxedResponseFields(
                                fieldWithPath("intraId").description("user's 42 intra Id"),
                                fieldWithPath("userImageUri").description("uri for user's 42 intra profile picture"),
                                fieldWithPath("isAdmin").description("Boolean to check if user is admin")
                        ))
                );
    }

    @Test
    @Order(2)
    @Transactional
    void userFindDetail() throws Exception {
        User user0 = realWorld.basicUser();
        User user1 = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/users/{targetUserId}/detail", user1.getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user0).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users-{targetUserId}-detail (유저 상세정보 조회)",
                        pathParameters(
                                parameterWithName("targetUserId").description("intra Id to find detail")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("intraId").description("user's 42 intra Id"),
                                fieldWithPath("userImageUri").description("uri for user's 42 intra profile picture"),
                                fieldWithPath("racketType").description("user's racket type"),
                                fieldWithPath("statusMessage").description("user's status message"),
                                fieldWithPath("level").description("user's current level"),
                                fieldWithPath("currentExp").description("user's current Exp for current level"),
                                fieldWithPath("maxExp").description("user max exp for current level"),
                                fieldWithPath("expRate").description("user's exp rate in current level"),
                                fieldWithPath("rivalRecord").description("login user's record versus target user")
                        )));
    }

    @Test
    @Order(3)
    @Transactional
    void userFindRank() throws Exception {
        User user = userRepository.findByIntraId("1").orElse(null);
        mockMvc.perform(get("/pingpong/users/{targetUserId}/rank", user.getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .param("season", "1")
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users-{targetUserId}-rank (유저 랭크정보 조회)",
                        pathParameters(
                                parameterWithName("targetUserId").description("intra Id to find rank")
                        ),
                        requestParameters(
                                parameterWithName("season").description("season of finding rank(OPTIONAL, defalt value: latest rank season")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("rank").description("user's rank for current season"),
                                fieldWithPath("ppp").description("user's ppp for current season"),
                                fieldWithPath("wins").description("user's win count for current season"),
                                fieldWithPath("losses").description("user's loss count for current season"),
                                fieldWithPath("winRate").description("user's win rate for current season")
                        ))
                );
    }

    @Test
    @Order(4)
    @Transactional
    void userFindHistorics() throws Exception {
        User user = userRepository.findByIntraId("1").orElse(null);
        mockMvc.perform(get("/pingpong/users/{userId}/historics", user.getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .param("season", "1")
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users-{userId}-historics (유저 ppp 히스토리 조회)",
                        pathParameters(
                                parameterWithName("userId").description("intra Id to find history")
                        ),
                        requestParameters(
                                parameterWithName("season").description("season of finding history(OPTIONAL, defalt value: latest rank season")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("historics").description("list of user ppp change histories"),
                                fieldWithPath("historics[].date").description("history date"),
                                fieldWithPath("historics[].ppp").description("history ppp")
                        ))
                );
    }

    @Test
    @Order(5)
    @Transactional
    void userModifyProfile() throws Exception {
        User user = realWorld.basicUser();
        Map<String, String> body5 = new HashMap<>();
        body5.put("racketType", RacketType.PENHOLDER.getCode());
        body5.put("statusMessage", "message");
        mockMvc.perform(put("/pingpong/users/detail").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body5))
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/PUT/users-detail (유저 프로필 변경)",
                        relaxedRequestFields(
                                fieldWithPath("racketType").description("RacketType[penholder, shakehand, dual]"),
                                fieldWithPath("statusMessage").description("status message(min length 0, max length 30")
                        ))
                );
    }

    @Test
    @Order(6)
    @Transactional
    void userSearchResult() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/users/searches").contentType(MediaType.APPLICATION_JSON)
                        .param("q", "b")
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users-searches (유저 검색)",
                        requestParameters(
                                parameterWithName("q").description("part or whole of finding id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("users").description("list of user intra Ids including query string")
                        ))
                );
    }

    @Test
    @Order(7)
    @Transactional
    void userLiveInfo() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/UserControllers/GET/users-live (유저 live 정보 조회)",
                        relaxedResponseFields(
                                fieldWithPath("notiCount").description("list of user ppp change histories"),
                                fieldWithPath("event").description("current event(null, match, game)"),
                                fieldWithPath("currentMatchMode").description("current match's mode(null, normal, rank)")
                        ))
                );
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}