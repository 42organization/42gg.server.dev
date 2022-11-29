package io.pp.arcade.v1.domain.rank.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.RacketType;
import org.assertj.core.api.Assertions;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private RankRepository rankRepository;

    @Autowired
    private RedisTemplate<String, RankRedis> redisRank;

    @Autowired
    private TestInitiator testInitiator;

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;
    @Value("${spring.data.web.pageable.default-page-size}")
    Integer defaultPageSize;

    List<User> users;
    List<User> preUsers;
    Team[] teams;
    String preSeason;
    Season season;
    List<Rank> ranks;
    RankListResponseDto responseDto;
    //Slot[] slots;
    @BeforeEach
    void init(){
        flushAll();
        testInitiator.letsgo();
        users = new ArrayList<>(Arrays.asList(testInitiator.users));
        teams = testInitiator.teams;
        preSeason = testInitiator.preSeason.getId().toString();
        preUsers = new ArrayList<>(users);
        season = testInitiator.testSeason;
        Collections.reverse(preUsers);
        /* init preSeason user*/
        ranks = new ArrayList<>();
        for (User user : users) {
            int idx = users.indexOf(user);
            Rank singleRank = Rank.builder().user(user).ppp(user.getPpp()).seasonId(season.getId()).racketType(RacketType.NONE).losses(0).wins(0).statusMessage(user.getIntraId()).gameType(GameType.SINGLE).ranking(0).build();
            ranks.add(singleRank);
        }
        rankRepository.saveAll(ranks);
    }
    @AfterEach
    void end(){
        redisRank.unwatch();
    }

    @Test
    @Transactional
    void rankList() throws Exception {
        ResultActions actions;
        String page;
        String count;
        Integer page_int;
        Integer count_int;

        /*
         * 메인 페이지 - 랭킹 조회
         * count = 3
         * season = null
         * */
        page = "1";
        count = "3";
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                .param("page",page)
                .param("count", count))
                .header("Authorization", "Bearer " + 0))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    responseDto = (RankListResponseDto)convertJSONStringToObject(json, RankListResponseDto.class);
                });

        //then
        Integer _page = Integer.parseInt(page);
        Integer _count = Integer.parseInt(count);
        checkRankListResponseDto(responseDto, _page, _count);
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-list-count-is-3"));

        /*
         * 랭킹 페이지 - 랭킹 조회
         * page = 2
         * count = 10
         * season = null
         * */
        page = "1";
        count = "10";

        // when
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                .param("page",page)
                .param("count", count)
                .header("Authorization", "Bearer " + 0)))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    responseDto = (RankListResponseDto)convertJSONStringToObject(json, RankListResponseDto.class);
                });

        //then
        page_int = Integer.parseInt(page);
        count_int = Integer.parseInt(count);
        checkRankListResponseDto(responseDto, page_int, count_int);
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-all-list-count-is-10-and-next-page"));

        /*
         * 랭킹 페이지 - 랭킹 조회
         * page = 1
         * count = null
         * season = null
         * */
        page = "1";

        // when
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                        .param("page",page)
                        .header("Authorization", "Bearer " + 0)))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    responseDto = (RankListResponseDto)convertJSONStringToObject(json, RankListResponseDto.class);
                });

        //then
        page_int = Integer.parseInt(page);
        count_int = defaultPageSize;
        checkRankListResponseDto(responseDto, page_int, count_int);
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-list-count-is-default-page-size"));


        /*
         * 랭킹 페이지 - 현 시즌 랭킹 조회
         * count = NULL
         * season = NULL
         * *//*
        page = "1";
        actions = mockMvc.perform((get("/pingpong/ranks/single").contentType(MediaType.APPLICATION_JSON)
                        .param("page",page))
                        .header("Authorization", "Bearer " + 0));
        for (int i = 0; i < Integer.parseInt(count); i++) {
            actions.andExpect(jsonPath("$.rankList["+ i +"].intraId").value(users.get(i).getIntraId()));
        }
        actions.andDo(document("v1-ranking-find-all-list"));

        *//*
         * 랭킹 페이지 - 이전 시즌 랭킹 조회
         * count = null
         * season = 1
         * *//*
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

        *//*
         * 랭킹 페이지 - 존재하지 않는 시즌 랭킹 조회
         * count = null
         * season = 100
         * *//*
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
        */
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

    private void checkRankListResponseDto(RankListResponseDto responseDto, Integer page, Integer count) {
        List<RankUserDto> rankList = responseDto.getRankList();
        /* 랭크 순위 확인*/
        for (RankUserDto rankUserDto : rankList) {
            Integer idx = rankList.indexOf(rankUserDto);
            Integer expectRank = (page - 1) * count + idx + 1;
            Integer actualRank = rankUserDto.getRank();
            Assertions.assertThat(actualRank).isEqualTo(expectRank);
        }
    }


    public static <T>  Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.readValue(json, objectClass);
    }
}