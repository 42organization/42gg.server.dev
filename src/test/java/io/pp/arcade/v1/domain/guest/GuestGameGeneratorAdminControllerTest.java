package io.pp.arcade.v1.domain.guest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class GuestGameGeneratorAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private RealWorld realWorld;

    @Autowired
    private SeasonRepository seasonRepository;

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;


    /* 슬롯 아이디로 게임 생성되는지 확인
        team 안에 user 넣고
        slot 안에 team 넣어서 slot 만들고!?~!
     */
    @Test
    @Transactional
    void 슬롯아이디_게임생성() throws Exception {
        User[] users = realWorld.getGuestUsers();
        User[] admins = realWorld.getAdminUsers();
        Token adminToken = tokenRepository.findByUserId(admins[0].getId()).orElseThrow();
        Slot slot = realWorld.getRankedSlotWithTwoUsersMinutesLater(users[0], users[1], 1);

        Map<String, String> body = new HashMap<>();
        body.put("slotId", slot.getId().toString());
        mockMvc.perform(post("/pingpong/admin/matchtrigger").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        Game game = gameRepository.findBySlotId(slot.getId()).orElseThrow();
        Assertions.assertThat(game.getSlot().getId()).isEqualTo(slot.getId());
        Assertions.assertThat(game.getStatus()).isEqualTo(StatusType.LIVE);
    }

    @BeforeEach
    void init() {
        RedisClient redisClient = RedisClient.create("redis://"+ host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
        seasonRepository.save(Season.builder()
                .seasonName("test")
                .pppGap(100)
                .startPpp(1000)
                .startTime(LocalDateTime.now().minusYears(1))
                .endTime(LocalDateTime.now().plusYears(1))
                .seasonMode(Mode.BOTH)
                .build());
    }
}
