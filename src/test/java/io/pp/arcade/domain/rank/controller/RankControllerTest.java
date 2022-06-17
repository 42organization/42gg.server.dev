package io.pp.arcade.domain.rank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.rank.RankService;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.util.GameType;
import io.pp.arcade.global.util.RacketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class RankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    User user;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
    HashMap<String, User> users ;

    @BeforeEach
    void init(){
        user = userRepository.save(User.builder().ppp(0).statusMessage("").intraId("donghyuk").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user2 = userRepository.save(User.builder().ppp(100).statusMessage("").intraId("nheo").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user3 = userRepository.save(User.builder().ppp(100).statusMessage("").intraId("hakim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user4 = userRepository.save(User.builder().ppp(100).statusMessage("").intraId("jiyun").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user5 = userRepository.save(User.builder().ppp(400).statusMessage("").intraId("jekim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user6 = userRepository.save(User.builder().ppp(500).statusMessage("").intraId("wochae").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());

        users = new HashMap<>();
        users.put("donghyuk", user);
        users.put("nheo", user2);
        users.put("hakim", user3);
        users.put("jiyun", user4);
        users.put("jekim", user5);
        users.put("wochae", user6);
        //rankRepository.save(Rank.builder().user(user).ranking(0).losses(0).wins(0).ppp(0).racketType(RacketType.SHAKEHAND).seasonId(0).build());
    }

    @Test
    void rankList() throws Exception {
        for (User user : users.values()) {
            RankRedis singleRank = RankRedis.from(user, GameType.SINGLE);
            RankRedis doubleRank = RankRedis.from(user, GameType.DOUBLE);
            redisTemplate.opsForValue().set(user.getIntraId() + GameType.SINGLE.getKey(), singleRank);
            redisTemplate.opsForValue().set(user.getIntraId() + GameType.DOUBLE.getKey(), doubleRank);
            redisTemplate.opsForZSet().add(GameType.SINGLE.getKey(), user.getIntraId() + GameType.SINGLE, user.getPpp());
            redisTemplate.opsForZSet().add(GameType.DOUBLE.getKey(), user.getIntraId() + GameType.DOUBLE, user.getPpp());
        }
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("userId", user.getId().toString());
        mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                .params(params)))
                .andExpect(status().isOk())
                .andDo(document("Ranking-List"));
    }
}