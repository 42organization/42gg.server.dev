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
import io.pp.arcade.v1.global.type.FeedbackType;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
public class FeedbackController {
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

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        realWorld.makeDefaultSeasons();
    }

    @Test
    @Transactional
    @Order(1)
    void feedbackSave() throws Exception {
        User user = realWorld.basicUser();

        Map<String, String> body = new HashMap<>();
        body.put("category", FeedbackType.CHEERS.name());
        body.put("content", "응원합니다!!");

        mockMvc.perform(post("/pingpong/feedback").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken())
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("V1/FeedbackController/POST/feedback-post (피드백 입력)",
                        relaxedRequestFields(
                                fieldWithPath("content").description("content of feedback"),
                                fieldWithPath("category").description("category of feedback")
                        )));
    }


    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}
