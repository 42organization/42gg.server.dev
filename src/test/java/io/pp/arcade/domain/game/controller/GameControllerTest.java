package io.pp.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.game.dto.GameResultRequestDto;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.dto.UserDto;
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
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private PChangeRepository pChangeRepository;

    User user;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    List<User> users;
    List<Slot> slotList;

    @BeforeEach
    void init() {
        user = User.builder().intraId("donghyuk").statusMessage("").ppp(1000).build();
        user1 = User.builder().intraId("nheo").statusMessage("").ppp(1000).build();
        user2 = User.builder().intraId("jekim").statusMessage("").ppp(1000).build();
        user3 = User.builder().intraId("jiyun").statusMessage("").ppp(1000).build();
        user4 = User.builder().intraId("wochae").statusMessage("").ppp(1000).build();
        user5 = User.builder().intraId("hakim").statusMessage("").ppp(1000).build();
        users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        for (User u : users) {
            userRepository.save(u);
        }
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        for (int i = 0; i < 18; i++) {
            Team team1 = teamRepository.save(Team.builder().teamPpp(0).headCount(0).score(0).build());
            Team team2 = teamRepository.save(Team.builder().teamPpp(0).headCount(0).score(0).build());
            LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                    15 + i / 6, (i * 10) % 60, 0); // 3시부터 10분 간격으로 18개 슬롯 생성
            slotRepository.save(Slot.builder()
                    .team1(team1)
                    .team2(team2)
                    .tableId(1)
                    .headCount(0)
                    .time(time)
                    .build());
        }
        slotList = slotRepository.findAll();
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
    Game saveGame(Slot slot, Team team1, Team team2) {
        Game game = Game.builder()
                .slot(slot)
                .team1(team1)
                .team2(team2)
                .type("single")
                .time(slot.getTime())
                .season(1)
                .status("end")
                .build();
        gameRepository.save(game);
        return game;
    }

    @Test
    @Transactional
    void gameUserInfo() throws Exception {
        Slot slot = slotList.get(0);
        Team team1 = slot.getTeam1();
        Team team2 = slot.getTeam2();
        addUserInTeam(team1, user, true);
        addUserInTeam(team2, user1, true);
        Game game = saveGame(slot, team1, team2);

        mockMvc.perform(get("/pingpong/games/"+ game.getId().toString() +"/result").contentType(MediaType.APPLICATION_JSON)
                .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("game-user-info"));
    }

    @Test
    @Transactional
    void saveGameResult() throws Exception {
        //given
        Slot slot = slotList.get(0);
        Team team1 = slot.getTeam1();
        Team team2 = slot.getTeam2();
        addUserInTeam(team1, user, true); //donghyuk
        addUserInTeam(team2, user1, true); // nheo
        Game game = saveGame(slot, team1, team2);

        //when
        Map<String, String> body = new HashMap<>();
        body.put("myTeamScore", "2");
        body.put("enemyTeamScore", "1");

        //then
        mockMvc.perform(post("/pingpong/games/"+ game.getId().toString() +"/result").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)) // { "myTeamScore" : "2", ..}
                .param("userId", user.getId().toString()))
                // ?userId=userId(donghyuk's userId) 어느 팀에 속한 유저인지 혹은 결과 입력이 필요한 유저가 맞는지 알기 위해서
                .andExpect(status().isOk())
                .andDo(document("save-game-result-single"));

        //given2
        slot = slotList.get(1);
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();
        addUserInTeam(team1, user2, true);
        addUserInTeam(team1, user3, false);
        addUserInTeam(team2, user4, true);
        addUserInTeam(team2, user5, false);
        game = saveGame(slot, team1, team2);
        
        //when2
        body = new HashMap<>();
        body.put("myTeamScore", "1");
        body.put("enemyTeamScore", "2");

        //then2
        mockMvc.perform(post("/pingpong/games/"+ game.getId().toString() +"/result").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)) // { "myTeamScore" : "2", ..}
                        .param("userId", user3.getId().toString()))
                // ?userId=userId(donghyuk's userId) 어느 팀에 속한 유저인지 혹은 결과 입력이 필요한 유저가 맞는지 알기 위해서
                .andExpect(status().isOk())
                .andDo(document("save-game-result-double"));
        MultiValueMap<String, String> params;
        params = new LinkedMultiValueMap<>();
        params.add("index", "1");
        params.add("count", "1");
        params.add("status", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-game-results"));
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