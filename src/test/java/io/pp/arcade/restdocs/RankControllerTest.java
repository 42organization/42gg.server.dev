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
import io.pp.arcade.v1.global.type.GameType;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestParametersSnippet.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
class RankControllerTest {
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

    User basic;
    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        Season[] seasons = realWorld.makeDefaultSeasons();
        realWorld.makeMixedGameResultsForDayAmongPastSeason(seasons[0], 120);
        basic = realWorld.basicUser();
        realWorld.getUsersWithVariousPPP();
    }

    @Test
    @Order(1)
    @Transactional
    void rankList() throws Exception {
        mockMvc.perform((get("/pingpong/ranks/{gametype}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("count", "10")
                .header("Authorization", "Bearer " + tokenRepository.findByUser(basic).getAccessToken())))
                .andExpect(status().isOk())
                .andDo(document("V1/RankControllers/GET/ranks-{gametype} (랭크 페이지 조회)",
                        requestParameters(
                                parameterWithName("page").description("page number(OPTIONAL, default value: 1").optional(),
                                parameterWithName("count").description("count to show on each page(OPTIONAL, default value: 20").optional(),
                                parameterWithName("season").description("querying season(OPTIONAL, default value: current season)").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("myRank").description("querying season's rank of requesting user"),
                                fieldWithPath("currentPage").description("current page on the season's rank page"),
                                fieldWithPath("totalPage").description("total page count of the season's rank page"),
                                fieldWithPath("rankList").description("list of user ranking infos"),
                                fieldWithPath("rankList[].intraId").description("user's intra Id"),
                                fieldWithPath("rankList[].rank").description("user's rank of the season"),
                                fieldWithPath("rankList[].ppp").description("user's ppp of the season"),
                                fieldWithPath("rankList[].wins").description("user's win count of the season"),
                                fieldWithPath("rankList[].losses").description("user's loss count of the season"),
                                fieldWithPath("rankList[].statusMessage").description("user's status message of the season"),
                                fieldWithPath("rankList[].winRate").description("user's win rate of the season")
                        ),
                        pathParameters(
                                parameterWithName("gametype").description("single / double")
                        ))
                );
    }

    @Test
    @Order(2)
    @Transactional
    void vipList() throws Exception {
        mockMvc.perform((get("/pingpong/vip").contentType(MediaType.APPLICATION_JSON))
                .param("page","2")
                .param("count", "10")
                .header("Authorization", "Bearer " + tokenRepository.findByUser(basic).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/RankControllers/GET/vip (vip 페이지 조회)",
                        requestParameters(
                                parameterWithName("page").description("page number(OPTIONAL, default value: 1").optional(),
                                parameterWithName("count").description("count to show on each page(OPTIONAL, default value: 20").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("myRank").description("vip rank of requesting user"),
                                fieldWithPath("currentPage").description("current page on the vip page"),
                                fieldWithPath("totalPage").description("total page count of the vippage"),
                                fieldWithPath("rankList").description("list of user vip infos"),
                                fieldWithPath("rankList[].intraId").description("user's intra Id"),
                                fieldWithPath("rankList[].rank").description("user's vip rank"),
                                fieldWithPath("rankList[].statusMessage").description("user's current status message"),
                                fieldWithPath("rankList[].level").description("user's level"),
                                fieldWithPath("rankList[].exp").description("user's exp on current level")
                        ))
                );;
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}