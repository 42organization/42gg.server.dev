package io.pp.arcade.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.DatabaseCleanup;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.game.Game;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RestDocsConfiguration.class)
@Transactional
public class GameControllerTest {
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

    User user1;
    User user2;
    Season[] seasons;

    @BeforeAll
    @Transactional
    void init() {
        flushAll();
        databaseCleanup.execute();
        seasons = realWorld.makeDefaultSeasons();
        realWorld.makeMixedGameResultsForDayAmongPastSeason(seasons[0], 120);
        user1 = realWorld.basicUser();
        user2 = realWorld.basicUser();
    }

    @Test
    @Order(1)
    @Transactional
    void gameResult() throws Exception {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("seasonId", "1");
        params.add("gameId", "100");
        params.add("count", "10");
        params.add("status", "end");
        params.add("mode", "rank");

        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/games-rank (게임 결과 목록)",
                        relaxedRequestParameters(
                                parameterWithName("seasonId").description("season Id, default : current season"),
                                parameterWithName("gameId").description("game Id, default : INT_MAX. returns the games under the given id"),
                                parameterWithName("count").description("numbers of games, default : 10"),
                                parameterWithName("status").description("game status : end(after entering the score), wait(after 15 minutes from start, without entering the score), live(before 15 minutes from start, without entering the score)"),
                                parameterWithName("mode").description("game mode : rank, normal, null. null means all modes")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("games").description("lists of games"),
                                fieldWithPath("games[].gameId").description("game Id"),
                                fieldWithPath("games[].type").description("game type : single, double"),
                                fieldWithPath("games[].status").description("game status : end, wait, live"),
                                fieldWithPath("games[].mode").description("game mode : rank, normal"),
                                fieldWithPath("games[].time").description("game time : yyyy-MM-dd HH:mm:ss"),
                                fieldWithPath("games[].team1").description("team1"),
                                fieldWithPath("games[].team1.players").description("team1 players"),
                                fieldWithPath("games[].team1.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team1.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team1.players[].wins").description("player's wins. only in rank mode"),
                                fieldWithPath("games[].team1.players[].losses").description("player's losses. only in rank mode"),
                                fieldWithPath("games[].team1.players[].winRate").description("player's winRate, not using. only in rank mode"),
                                fieldWithPath("games[].team1.players[].pppChange").description("player's pppChange, not using. only in rank mode"),
                                fieldWithPath("games[].team1.isWin").description("team1 is win or not. only in rank mode"),
                                fieldWithPath("games[].team1.score").description("team1 score. only in rank mode"),
                                fieldWithPath("games[].team2").description("team2"),
                                fieldWithPath("games[].team2.players").description("team2 players"),
                                fieldWithPath("games[].team2.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team2.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team2.players[].wins").description("player's wins. only in rank mode"),
                                fieldWithPath("games[].team2.players[].losses").description("player's losses. only in rank mode"),
                                fieldWithPath("games[].team2.players[].winRate").description("player's winRate, not using. only in rank mode"),
                                fieldWithPath("games[].team2.players[].pppChange").description("player's pppChange, not using. only in rank mode"),
                                fieldWithPath("games[].team2.isWin").description("team2 is win or not. only in rank mode"),
                                fieldWithPath("games[].team2.score").description("team2 score. only in rank mode"),
                                fieldWithPath("lastGameId").description("last game Id. client use this value for next request"),
                                fieldWithPath("isLast").description("is last page or not")
                        )));

        params.clear();

        params.add("seasonId", "1");
        params.add("gameId", String.valueOf(Integer.MAX_VALUE));
        params.add("count", "10");
        params.add("status", "end");
        params.add("mode", "normal");

        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/games-normal (게임 결과 목록)",
                        relaxedRequestParameters(
                                parameterWithName("seasonId").description("season Id, default : current season"),
                                parameterWithName("gameId").description("game Id, default : INT_MAX. returns the games under the given id"),
                                parameterWithName("count").description("numbers of games, default : 10"),
                                parameterWithName("status").description("game status : end(after entering the score), wait(after 15 minutes from start, without entering the score), live(before 15 minutes from start, without entering the score)"),
                                parameterWithName("mode").description("game mode : rank, normal, null. null means all modes")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("games").description("lists of games"),
                                fieldWithPath("games[].gameId").description("game Id"),
                                fieldWithPath("games[].type").description("game type : single, double"),
                                fieldWithPath("games[].status").description("game status : end, wait, live"),
                                fieldWithPath("games[].mode").description("game mode : rank, normal"),
                                fieldWithPath("games[].time").description("game time : yyyy-MM-dd HH:mm:ss"),
                                fieldWithPath("games[].team1").description("team1"),
                                fieldWithPath("games[].team1.players").description("team1 players"),
                                fieldWithPath("games[].team1.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team1.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team1.players[].level").description("player's level. only in normal mode"),
                                fieldWithPath("games[].team2").description("team2"),
                                fieldWithPath("games[].team2.players").description("team2 players"),
                                fieldWithPath("games[].team2.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team2.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team2.players[].level").description("player's level. only in normal mode"),
                                fieldWithPath("lastGameId").description("last game Id. client use this value for next request"),
                                fieldWithPath("isLast").description("is last page or not")
                        )));

        params.clear();

        params.add("seasonId", "1");
        params.add("gameId", String.valueOf(Integer.MAX_VALUE));
        params.add("count", "10");
        params.add("status", "live");
        params.add("mode", null);

        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/games-all (게임 결과 목록)",
                        relaxedRequestParameters(
                                parameterWithName("seasonId").description("season Id, default : current season"),
                                parameterWithName("gameId").description("game Id, default : INT_MAX. returns the games under the given id"),
                                parameterWithName("count").description("numbers of games, default : 10"),
                                parameterWithName("status").description("game status : end(after entering the score), wait(after 15 minutes from start, without entering the score), live(before 15 minutes from start, without entering the score)"),
                                parameterWithName("mode").description("game mode : rank, normal, null. null means all modes")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("games").description("lists of games"),
                                fieldWithPath("games[].gameId").description("game Id"),
                                fieldWithPath("games[].type").description("game type : single, double"),
                                fieldWithPath("games[].status").description("game status : end, wait, live"),
                                fieldWithPath("games[].mode").description("game mode : rank, normal"),
                                fieldWithPath("games[].time").description("game time : yyyy-MM-dd HH:mm:ss"),
                                fieldWithPath("games[].team1").description("team1"),
                                fieldWithPath("games[].team1.players").description("team1 players"),
                                fieldWithPath("games[].team1.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team1.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team2").description("team2"),
                                fieldWithPath("games[].team2.players").description("team2 players"),
                                fieldWithPath("games[].team2.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team2.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("lastGameId").description("last game Id. client use this value for next request"),
                                fieldWithPath("isLast").description("is last page or not")
                        )));
    }

    @Test
    @Order(2)
    @Transactional
    void gameNormalSave() throws Exception {
        realWorld.makeUsersHaveCurrentMatchMatchedImminentGameStatusLive(Mode.NORMAL, seasons[2], user1, user2);

        mockMvc.perform(post("/pingpong/games/result/normal")
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isCreated())
                .andDo(document("V1/GameControllers/POST/result-normal (게임 종료 요청 - 일반)"));
    }

    @Test
    @Order(3)
    @Transactional
    void gameRankSave() throws Exception {
        realWorld.makeUsersHaveCurrentMatchMatchedImminentGameStatusLive(Mode.RANK, seasons[2], user1, user2);

        Map<String, String> body = new HashMap<>();
        body.put("myTeamScore", "2");
        body.put("enemyTeamScore", "1");

        mockMvc.perform(post("/pingpong/games/result/rank").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken())
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andDo(document("V1/GameControllers/POST/result-rank (게임 종료 요청 - 랭크)",
                        relaxedRequestFields(
                                fieldWithPath("myTeamScore").description("my team score"),
                                fieldWithPath("enemyTeamScore").description("enemy team score")
                        )));
    }

    @Test
    @Order(4)
    @Transactional
    void gameExpAndPppResult() throws Exception {
        CurrentMatch[] currentMatches = realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user1, user2, 2, 1);
        Game game = currentMatches[0].getGame();

        mockMvc.perform(get("/pingpong/games/{gameId}/result/rank", game.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/result-rank (단일 게임 결과 - 랭크)",
                        relaxedResponseFields(
                                fieldWithPath("beforeExp").description("exp before game end"),
                                fieldWithPath("beforeMaxExp").description("past level max exp"),
                                fieldWithPath("beforeLevel").description("level before game end"),
                                fieldWithPath("increasedExp").description("increased exp"),
                                fieldWithPath("increasedLevel").description("increased level"),
                                fieldWithPath("afterMaxExp").description("current level's max exp"),
                                fieldWithPath("changedPpp").description("ppp changed"),
                                fieldWithPath("beforePpp").description("ppp before game end")
                        )));
    }

    @Test
    @Order(5)
    @Transactional
    void gameExpResult() throws Exception {
        CurrentMatch[] currentMatches = realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.NORMAL, seasons[2], user1, user2, 2, 1);
        Game game = currentMatches[0].getGame();

        mockMvc.perform(get("/pingpong/games/{gameId}/result/normal", game.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/result-normal (단일 게임 결과 - 일반)",
                        relaxedResponseFields(
                                fieldWithPath("beforeExp").description("exp before game end"),
                                fieldWithPath("beforeMaxExp").description("past level max exp"),
                                fieldWithPath("beforeLevel").description("level before game end"),
                                fieldWithPath("increasedExp").description("increased exp"),
                                fieldWithPath("increasedLevel").description("increased level"),
                                fieldWithPath("afterMaxExp").description("current level's max exp")
                        )));
    }

    @Test
    @Order(6)
    @Transactional
    void gameResultByUserIdAndByGameIdAndCount() throws Exception {
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user1, user2, 2, 1);
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user2, user1, 2, 1);
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user1, user2, 2, 1);
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user2, user1, 2, 1);
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user1, user2, 2, 1);
        realWorld.makeUsersHaveCurrentMatchAndGameStatusEnd(Mode.RANK, seasons[2], user2, user1, 2, 1);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("count", "10");
        params.add("gameId", String.valueOf(Integer.MAX_VALUE));
        params.add("season", seasons[2].getId().toString());
        params.add("mode", null);

        mockMvc.perform(get("/pingpong/games/users/{intraId}", user1.getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer " + tokenRepository.findByUser(user1).getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("V1/GameControllers/GET/user-game-all (유저별 게임 결과 - 전체)",
                        relaxedRequestParameters(
                                parameterWithName("season").description("season Id, default : current season / why not seasonId?"),
                                parameterWithName("gameId").description("game Id, default : INT_MAX. returns the games under the given id"),
                                parameterWithName("count").description("numbers of games, default : 10"),
                                parameterWithName("mode").description("game mode : rank, normal, null. null means all modes")
                        ),
                        relaxedResponseFields(                                fieldWithPath("games").description("lists of games"),
                                fieldWithPath("games[].gameId").description("game Id"),
                                fieldWithPath("games[].type").description("game type : single, double"),
                                fieldWithPath("games[].status").description("game status : end, wait, live"),
                                fieldWithPath("games[].mode").description("game mode : rank, normal"),
                                fieldWithPath("games[].time").description("game time : yyyy-MM-dd HH:mm:ss"),
                                fieldWithPath("games[].team1").description("team1"),
                                fieldWithPath("games[].team1.players").description("team1 players"),
                                fieldWithPath("games[].team1.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team1.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team1.players[].wins").description("player's wins. only in rank mode"),
                                fieldWithPath("games[].team1.players[].losses").description("player's losses. only in rank mode"),
                                fieldWithPath("games[].team1.players[].winRate").description("player's winRate, not using. only in rank mode"),
                                fieldWithPath("games[].team1.players[].pppChange").description("player's pppChange, not using. only in rank mode"),
                                fieldWithPath("games[].team1.isWin").description("team1 is win or not. only in rank mode"),
                                fieldWithPath("games[].team1.score").description("team1 score. only in rank mode"),
                                fieldWithPath("games[].team2").description("team2"),
                                fieldWithPath("games[].team2.players").description("team2 players"),
                                fieldWithPath("games[].team2.players[].intraId").description("player's intraId"),
                                fieldWithPath("games[].team2.players[].userImageUri").description("player's userImageUri"),
                                fieldWithPath("games[].team2.players[].wins").description("player's wins. only in rank mode"),
                                fieldWithPath("games[].team2.players[].losses").description("player's losses. only in rank mode"),
                                fieldWithPath("games[].team2.players[].winRate").description("player's winRate, not using. only in rank mode"),
                                fieldWithPath("games[].team2.players[].pppChange").description("player's pppChange, not using. only in rank mode"),
                                fieldWithPath("games[].team2.isWin").description("team2 is win or not. only in rank mode"),
                                fieldWithPath("games[].team2.score").description("team2 score. only in rank mode"),
                                fieldWithPath("lastGameId").description("last game Id. client use this value for next request"),
                                fieldWithPath("isLast").description("is last page or not")
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
