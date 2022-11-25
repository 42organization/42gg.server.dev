package io.pp.arcade.v1.domain.season.guest;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.guest.DogUtil;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
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

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class GuestLoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DogUtil dogUtil;

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;
    @Value("${info.web.frontUrl}")
    private String frontUrl;

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

    @Test
    @Transactional
    void 신규_게스트로그인() throws Exception {
        // given
        mockMvc.perform(get("/pingpong/login/guest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
        // when
        int cnt = userRepository.findAll().size();
        User user = userRepository.findAll().get(0);
        Token token = tokenRepository.findAll().get(0);
        // then
        Assertions.assertThat(cnt).isEqualTo(1);
        System.out.println(user.toString());
        System.out.println(token.toString());
    }

    @Test
    @Transactional
    void 기존_게스트로그인() throws Exception {
        // given
        User user1 = User.builder()
                .intraId("귀여운리트리버")
                .imageUri("리트리버.png")
                .statusMessage("리트리버는 귀여워요")
                .racketType(RacketType.NONE)
                .ppp(1000)
                .totalExp(0)
                .roleType(RoleType.USER)
                .eMail("리트리버@pp.com")
                .build();
        userRepository.save(user1);
        Token token1 = new Token(user1, "abcdefg", "abcdefg");
        tokenRepository.save(token1);

        mockMvc.perform(get("/pingpong/login/guest").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token1.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(frontUrl));
        // when
        int cnt = userRepository.findAll().size();
        User resultUser = userRepository.findAll().get(0);
        // then
        Assertions.assertThat(cnt).isEqualTo(1);
        System.out.println(resultUser.toString());
    }

    @Test
    @Transactional
    void 유효하지않은토큰_게스트로그인() throws Exception {
        // given
        mockMvc.perform(get("/pingpong/login/guest").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + "울랄라"))
                .andExpect(status().is3xxRedirection());
        // when
        int cnt = userRepository.findAll().size();
        User resultUser = userRepository.findAll().get(0);
        // then
        Assertions.assertThat(cnt).isEqualTo(1);
        System.out.println(resultUser.toString());
    }
}