package io.pp.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;

    Slot slot;
    Team team1;
    Team team2;

    @BeforeEach
    void init() {
        initiator.letsgo();
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
    void gameUserInfo() throws Exception {
        Team team1 = slot.getTeam1();
        Team team2 = slot.getTeam2();
        addUserInTeam(team1, user1, true);
        addUserInTeam(team2, user2, true);
        Game game = saveGame(slot, team1, team2);
        addCurrentMatch(game, user1);
        addCurrentMatch(game, user2);


        mockMvc.perform(get("/pingpong/games/result").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("game-user-info"));
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
        mockMvc.perform(post("/pingpong/games/result").contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(post("/pingpong/games/result").contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(post("/pingpong/games/result").contentType(MediaType.APPLICATION_JSON)
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
//
//    @Test
//    @Transactional
//    void gameResultByUserIdAndIndexAndCount() throws Exception {
//    }
}