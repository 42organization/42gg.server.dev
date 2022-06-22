package io.pp.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class GameControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    TestInitiator initiator;
    private final int GAMESIZE = 200;
    @Autowired
    private PChangeRepository pChangeRepository;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;

    Slot slot;
    Team team1;
    Team team2;
    Game liveGame;
    Game waitGame;
    User[] users;
    Team[] teams;
    Game[] endGames;
    Slot[] slots;
    @BeforeEach
    void init() {
        initiator.letsgo();
        users = initiator.users;
        teams = initiator.teams;
        slots = initiator.slots;
        endGames = new Game[GAMESIZE - 2];

        for (int i = 0; i < GAMESIZE - 2; i++){
            endGames[i] = gameRepository.save(Game.builder().slot(slots[0]).team1(teams[0]).team2(teams[1]).type(GameType.SINGLE).time(slots[0].getTime()).season(1).status(StatusType.END).build());
        }
        waitGame = gameRepository.save(Game.builder().slot(slots[0]).team1(teams[0]).team2(teams[1]).type(GameType.SINGLE).time(slots[0].getTime()).season(1).status(StatusType.WAIT).build());
        liveGame = gameRepository.save(Game.builder().slot(slots[0]).team1(teams[0]).team2(teams[1]).type(GameType.SINGLE).time(slots[0].getTime()).season(1).status(StatusType.LIVE).build());

        /* pChange 생성 */
        for (int i = 0; i < GAMESIZE - 2; i++){
            pChangeRepository.save(PChange.builder().game(endGames[i]).user(users[0]).pppChange(20).pppResult(1000).build());
            pChangeRepository.save(PChange.builder().game(endGames[i]).user(users[1]).pppChange(20).pppResult(1000).build());
        }
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];
        user5 = initiator.users[4];
        user6 = initiator.users[5];

        slot = initiator.slots[0];
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();
    }

    @Transactional
    void addUserInTeam(Team team, User user, boolean is1){
        if (is1)
            team.setUser1(user);
        else
            team.setUser2(user);
        team.setTeamPpp(user.getPpp());
        team.setHeadCount(1);
    }

    @Transactional
    void addCurrentMatch(Game game, User user) {
        currentMatchRepository.save(CurrentMatch.builder()
                .matchImminent(true)
                .isMatched(true)
                .game(game)
                .slot(game.getSlot())
                .user(user)
                .build());
    }

    @Transactional
    Game saveGame(Slot slot, Team team1, Team team2) {
        Game game = Game.builder()
                .slot(slot)
                .team1(team1)
                .team2(team2)
                .type(GameType.BUNGLE)
                .time(slot.getTime())
                .season(1)
                .status(StatusType.LIVE)
                .build();
        gameRepository.save(game);
        return game;
    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - /games")
    void gameUserInfo() throws Exception {
        /*
         * gameId != Integer (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gameId", "string");
        params.add("count", "20");
        params.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isBadRequest());

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params2)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk());

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params3)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk());

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(status().isOk());

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(status().isOk());

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "1234");
        params4.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk());

        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "1234");
        params5.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params5)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(status().isOk());

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params6)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(liveGame.getId()))
                .andExpect(jsonPath("$.games[1].gameId").value(waitGame.getId()))
                .andExpect(jsonPath("$.games[2].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("game-user-info"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params7)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(liveGame.getId()))
                .andExpect(jsonPath("$.games[1].gameId").value(waitGame.getId()))
                .andExpect(jsonPath("$.games[2].gameId").value(endGames[endGames.length - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("game-user-info"));

        /*
         * GameId -> 100
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "100");
        params8.add("count", "20");
        params8.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                .params(params8)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games[0].gameId").value(endGames[99 - 1].getId()))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void saveGameResult() throws Exception {
        //given
        Team team1 = slot.getTeam1();
        Team team2 = slot.getTeam2();
        addUserInTeam(team1, user1, true); //donghyuk
        addUserInTeam(team2, user4, true); // nheo
        Game game = saveGame(slot, team1, team2);
        addCurrentMatch(game, user1);
        addCurrentMatch(game, user4);

        //when
        Map<String, String> body = new HashMap<>();
        body.put("myTeamScore", "2");
        body.put("enemyTeamScore", "1");


        //then
        mockMvc.perform(post("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)) // { "myTeamScore" : "2", ..}
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("save-game-result-single"));

        //given2
        slot = initiator.slots[1];
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();
        addUserInTeam(team1, user2, true);
        addUserInTeam(team1, user3, false);
        addUserInTeam(team2, user5, true);
        addUserInTeam(team2, user6, false);
        Game game2 = saveGame(slot, team1, team2);
        addCurrentMatch(game2, user2);
        addCurrentMatch(game2, user3);

        addCurrentMatch(game2, user5);
        addCurrentMatch(game2, user6);

        //when2
        body = new HashMap<>();
        body.put("myTeamScore", "1");
        body.put("enemyTeamScore", "2");

        //then2
        mockMvc.perform(post("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)) // { "myTeamScore" : "2", ..}
                        .header("Authorization", "Bearer " + initiator.tokens[2].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("save-game-result-double"));
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
//        Pageable pageable;
        params.add("count", "10");
        params.add("status", StatusType.END.toString());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-game-results"));

        //given2 // 결과 재입력, 202에러 띄워야함
        //위의 게임을 그대로 씀

        //when2
        body = new HashMap<>();
        body.put("myTeamScore", "2");
        body.put("enemyTeamScore", "1");

        //then2
        mockMvc.perform(post("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)) // { "myTeamScore" : "2", ..}
                        .header("Authorization", "Bearer " + initiator.tokens[2].getAccessToken()))
                // ?userId=userId(donghyuk's userId) 어느 팀에 속한 유저인지 혹은 결과 입력이 필요한 유저가 맞는지 알기 위해서
                .andExpect(status().isAccepted())
                .andDo(document("save-game-result-after-duplicated-request"));
        params = new LinkedMultiValueMap<>();
//
//        params.add("gameId", "1");
        params.add("count", "10");
//        params.add("status", "end");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-game-results-after-duplicated-request"));

        params = new LinkedMultiValueMap<>();
//        Pageable pageable;
        params.add("count", "10");
        params.add("gameId", "3");

        mockMvc.perform(get("/pingpong/users/" + user4.getIntraId() + "/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-game-results-after-duplicated-request2"));

        mockMvc.perform(get("/pingpong/users/" + user5.getIntraId() + "/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-game-results-after-duplicated-request3"));
    }

//    @Test
//    @Transactional
//    void gameResultByCount() throws Exception {
//        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
//                .param())
//    }
//
//    @Test
//    @Transactional
//    void gameResultByIndexAndCount() throws Exception {
//    }

    @Test
    @Transactional
    void gameResultByUserIdAndIndexAndCount() throws Exception {
        /*
         * IntraId != null (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gameId", "string");
        params.add("count", "20");
        params.add("status", StatusType.END.toString());
        mockMvc.perform(get("/users/{intraId}/games", users[0].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isBadRequest());
    }
}