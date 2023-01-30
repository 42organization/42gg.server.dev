package io.pp.arcade.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.oauth.v2.repository.UserRefreshTokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.Mode;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
public class CurrentMatchController {
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

    Season[] seasons;

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        seasons = realWorld.makeDefaultSeasons();
    }

    @Test
    @Order(1)
    @Transactional
    public void currentMatchFind() throws Exception {
        User user1 = realWorld.basicUser();
        User user2 = realWorld.basicUser();
        realWorld.makeUsersHaveCurrentMatchMatchedImminentGameStatusLive(Mode.RANK, seasons[2], user1, user2);
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/CurrentMatchController/GET/currentMatch (현재 매치 정보)",
                        relaxedResponseFields(
                                fieldWithPath("time").description("slot's time"),
                                fieldWithPath("slotId").description("slot id"),
                                fieldWithPath("myTeam").description("my team. for double match(not useful)"),
                                fieldWithPath("enemyTeam").description("enemy teams. can be null(not matched / not imminent)"),
                                fieldWithPath("isMatched").description("is matched or not")
                        )));
    }

    @Test
    @Order(2)
    @Transactional
    void deleteCurrentMatch() throws Exception{
        User user1 = realWorld.basicUser();
        User user2 = realWorld.basicUser();
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user1, user2, 2, 0);
        mockMvc.perform(put("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/CurrentMatchController/PUT/deleteCurrentMatch (현재 매치 정보 삭제)"));
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}
