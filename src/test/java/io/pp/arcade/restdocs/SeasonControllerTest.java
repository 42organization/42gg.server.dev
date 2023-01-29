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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
class SeasonControllerTest {
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
    void getRankSeasonList() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/seasonlist").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/SeasonControllers/GET/seasonlist (시즌 목록 조회)",
                        relaxedResponseFields(
                                fieldWithPath("seasonMode").description("mode of current season"),
                                fieldWithPath("seasonList").description("list of seasons"),
                                fieldWithPath("seasonList[].id").description("id of each season"),
                                fieldWithPath("seasonList[].name").description("name of each season")
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