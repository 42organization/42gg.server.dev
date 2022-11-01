package io.pp.arcade.domain.rank.controller;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.rank.Rank;
import io.pp.arcade.v1.domain.rank.RankRedis;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.RacketType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private RankRepository rankRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, RankRedis> redisRank;

    @Autowired
    private TestInitiator testInitiator;

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;


    List<User> users;
    List<User> preUsers;
    Team[] teams;
    String preSeason;
    //Slot[] slots;
    @BeforeEach
    void init(){
        flushAll();
        testInitiator.letsgo();
        users = new ArrayList<>(Arrays.asList(testInitiator.users));
        teams = testInitiator.teams;
        preSeason = testInitiator.preSeason.getId().toString();
        preUsers = new ArrayList<>(users);
        Collections.reverse(preUsers);
        /* init preSeason user*/
        for (int i = 0; i < preUsers.size(); ++i)
            rankRepository.save(Rank.builder().seasonId(Integer.parseInt(preSeason)).user(preUsers.get(i)).ranking(i + 1).losses(0).wins(1).ppp(1000).racketType(RacketType.SHAKEHAND).gameType(GameType.SINGLE).build());
    }
    @AfterEach
    void end(){
        redisTemplate.unwatch();
        redisRank.unwatch();
    }

    @Test
    @Transactional
    void rankList() throws Exception {
        final GameType type = GameType.SINGLE;
        ResultActions actions;
        String page;
        String count;
        String season;

        /*
         * 메인 페이지 - 현 시즌 랭킹 조회
         * count = 3
         * season = null
         * */
        page = "1";
        count = "3";
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                .param("page",page)
                .param("count", count))
                .header("Authorization", "Bearer " + 0));
        for (int i = 0; i < Integer.parseInt(count); i++) {
            actions.andExpect(jsonPath("$.rankList["+ i +"].intraId").value(users.get(i).getIntraId()));
        }
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-list-count-is-3"));

        /*
         * 랭킹 페이지 - 현 시즌 랭킹 조회
         * count = NULL
         * season = NULL
         * */
        page = "1";
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                        .param("page",page))
                        .header("Authorization", "Bearer " + 0));
        for (int i = 0; i < Integer.parseInt(count); i++) {
            actions.andExpect(jsonPath("$.rankList["+ i +"].intraId").value(users.get(i).getIntraId()));
        }
        actions.andDo(document("v1-ranking-find-all-list"));

        /*
         * 랭킹 페이지 - 다음 페이지 조회
         * page = 2
         * count = 10
         * season = 1
         * */
        page = "2";
        count = "10";
        season = "1";
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                .param("page",page)
                .param("count", count)
                .param("season", season))
                .header("Authorization", "Bearer " + 0));
        for (int i = 0, size = Integer.parseInt(count); size < users.size(); i++, size++) {
            actions.andExpect(jsonPath("$.rankList["+ i +"].intraId").value(preUsers.get(Integer.parseInt(count) + i).getIntraId()));
        }
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-all-list-count-is-10-and-next-page"));


        /*
         * 랭킹 페이지 - 이전 시즌 랭킹 조회
         * count = null
         * season = 1
         * */
        page = "1";
        season = preSeason;
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                        .param("page",page)
                        .param("season", season))
                        .header("Authorization", "Bearer " + 0));
        for (User preUser : preUsers) {
            int i = preUsers.indexOf(preUser);
            actions.andExpect(jsonPath("$.rankList["+ i +"].intraId").value(preUsers.get(i).getIntraId()));
        }
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-all-list-pre-season"));

        /*
         * 랭킹 페이지 - 존재하지 않는 시즌 랭킹 조회
         * count = null
         * season = 100
         * */
        page = "1";
        season = "100";
        mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                        .param("page",page)
                        .param("season", season))
                        .header("Authorization", "Bearer " + 0))
                .andExpect(jsonPath("$.myRank").value(-1))
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.rankList.length()").value(0))
                .andExpect(status().isOk())
                .andDo(document("v1-ranking-find-all-list-wrong-season"));
    }

    private String getUserRankKey(String intraId, GameType gameType) {
        return intraId + gameType.getCode();
    }

    private String getRankKey(GameType gameType) {
        return gameType.getCode();
    }


    private Integer getRanking(RankRedis userInfo ,GameType gameType){
        Integer totalGames = userInfo.getLosses() + userInfo.getWins();
        Integer ranking = (totalGames == 0) ? -1 : redisRank.opsForZSet().reverseRank(getRankKey(gameType), getUserRankKey(userInfo.getIntraId(), gameType)).intValue() + 1;
        return ranking;
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://"+ host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}