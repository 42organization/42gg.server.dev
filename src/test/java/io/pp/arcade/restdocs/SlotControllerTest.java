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
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.GameType;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
class SlotControllerTest {
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

    Slot emptySlot;
    Slot deleteSlot;
    User deleteUser;

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        Season[] seasons = realWorld.makeDefaultSeasons();
        realWorld.makeMixedGameResultsForDayAmongPastSeason(seasons[0], 120);

        realWorld.getExpiredSlot();
        realWorld.getExpiredSlot();

        emptySlot = realWorld.getEmptySlotMinutesLater(10);
        realWorld.getNormalSlotWithOneUserMinutesLater(realWorld.basicUser(), 20);
        realWorld.getNormalSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 30);
        realWorld.getRankedSlotWithOneUserMinutesLater(realWorld.basicUser(), 40);
        realWorld.getRankedSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 50);
        realWorld.getEmptySlotMinutesLater(60);
        realWorld.getNormalSlotWithOneUserMinutesLater(realWorld.basicUser(), 70);
        realWorld.getNormalSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 80);
        realWorld.getRankedSlotWithOneUserMinutesLater(realWorld.basicUser(), 90);
        realWorld.getRankedSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 100);
        realWorld.getEmptySlotMinutesLater(110);
        realWorld.getNormalSlotWithOneUserMinutesLater(realWorld.basicUser(), 120);
        realWorld.getNormalSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 130);
        realWorld.getRankedSlotWithOneUserMinutesLater(realWorld.basicUser(), 140);
        realWorld.getRankedSlotWithTwoUsersMinutesLater(realWorld.basicUser(), realWorld.basicUser(), 150);

        deleteUser = realWorld.basicUser();
        deleteSlot = realWorld.getNormalSlotWithOneUserMinutesLater(deleteUser, 160);
    }

    @Test
    @Order(1)
    @Transactional
    void slotStatusList() throws Exception {
        User user = realWorld.basicUser();
        mockMvc.perform(get("/pingpong/match/tables/{tableId}/{mode}/{type}", "1", Mode.RANK.getCode(), GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/SlotControllers/GET/{tableId}-{mode}-{type} (슬롯 목록 조회)",
                        pathParameters(
                                parameterWithName("tableId").description("table id for the match(currently all is 1)"),
                                parameterWithName("mode").description("querying mode(normal / rank)"),
                                parameterWithName("type").description("querying type(single / double")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("intervalMinute").description("slot's interval minute"),
                                fieldWithPath("matchBoards").description("list of hourly match boards list"),
                                fieldWithPath("matchBoards[]").description("match boards for an hour"),
                                fieldWithPath("matchBoards[][].slotId").description("slot's id"),
                                fieldWithPath("matchBoards[][].time").description("slot's time"),
                                fieldWithPath("matchBoards[][].status").description("slot's status(open/close)"),
                                fieldWithPath("matchBoards[][].headCount").description("slot's head count"),
                                fieldWithPath("matchBoards[][].mode").description("slot's mode(null/normal/rank)")
                        ))
                );

    }

    @Test
    @Order(2)
    @Transactional
    void slotAddUser() throws Exception {
        User slotUser = realWorld.basicUser();
        Map<String, String> body = new HashMap<>();
        body.put("slotId", emptySlot.getId().toString());
        body.put("mode", Mode.RANK.getCode());
        mockMvc.perform(post("/pingpong/match/tables/{tableId}/{type}", "1", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(slotUser).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/SlotControllers/POST/match-tables-{tableId}-{type} (슬롯 참여)",
                        pathParameters(
                                parameterWithName("tableId").description("table id for the match(currently all is 1)"),
                                parameterWithName("type").description("querying type(single / double")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("slotId").description("id for slot to enter(Not Null, Positive)"),
                                fieldWithPath("mode").description("mode to play(normal, rank)")
                        ))
                );
    }

    @Test
    @Order(3)
    @Transactional
    void slotRemoveUser() throws Exception {
        mockMvc.perform(delete("/pingpong/match/slots/{slotId}", deleteSlot.getId()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(deleteUser).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/SlotControllers/DELETE/match-slots-{slotId} (슬롯 취소)",
                        pathParameters(
                                parameterWithName("slotId").description("slot id to delete")
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