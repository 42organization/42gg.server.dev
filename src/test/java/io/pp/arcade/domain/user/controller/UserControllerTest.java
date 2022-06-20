package io.pp.arcade.domain.user.controller;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.security.jwt.TokenRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.RestDocsConfiguration;
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

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class UserControllerTest {
    @Autowired
    TestInitiator testInitiator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PChangeRepository pChangeRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenRepository tokenRepository;

    User user;
    User user2;
    User user3;
    User user4;

    Game game;
    CurrentMatch currentMatch;
    CurrentMatch currentMatch2;
    Team team1;
    Team team2;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user = userRepository.findByIntraId("hakim").orElse(null);
        user2 = userRepository.findByIntraId("daekim").orElse(null);
        user3 = userRepository.findByIntraId("donghyuk").orElse(null);
        user4 = userRepository.findByIntraId("kipark").orElse(null);

        PChange pChange;
        Team team1 = teamRepository.save(Team.builder().teamPpp(0)
                .user1(user).headCount(1).score(0).build());
        Team team2 = teamRepository.save(Team.builder().teamPpp(0)
                .user1(user2).headCount(1).score(0).build());
        Slot slot = slotRepository.save(Slot.builder()
                .team1(team1)
                .team2(team2)
                .headCount(2)
                .type(GameType.SINGLE)
                .tableId(1)
                .time(LocalDateTime.now().plusDays(1))
                .gamePpp(50)
                .build());
        game = gameRepository.save(Game.builder().slot(slot).team1(team1).team2(team2).type(slot.getType()).time(slot.getTime()).season(1).status(StatusType.END).build());

        pChange = pChangeRepository.save(PChange.builder()
                .game(game)
                .user(user)
                .pppChange(2)
                .pppResult(2 + user.getPpp())
                .build());
        currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .game(game)
                .slot(slot)
                .user(user)
                .matchImminent(false)
                .isMatched(false)
                .build());

        currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .game(null)
                .slot(slot)
                .user(user4)
                .matchImminent(false)
                .isMatched(false)
                .build());
    }

    @Test
    @Transactional
    void findUser() throws Exception {
        mockMvc.perform(get("/pingpong/users").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user.getId().toString())
                            .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("find-user"));
    }

    @Test
    @Transactional
    void findDetailUser() throws Exception { // rank가 문제라서 안됐다!
        mockMvc.perform(get("/pingpong/users/" + user.getIntraId() + "/detail").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("find-user-detail"));
    }

    @Test
    @Transactional
    void findUserHistorics() throws Exception {
        mockMvc.perform(get("/pingpong/users/" + user.getIntraId().toString() + "/historics").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("find-user-historics"));
    }

    @Test
    @Transactional
    void findByPartsOfIntraId() throws Exception {
        mockMvc.perform(get("/pingpong/users/searches").contentType(MediaType.APPLICATION_JSON)
                        .param("q", "k")
                .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("search-user-with-partial-string"));
    }

    @Test
    @Transactional
    void userLiveInfo() throws Exception {
        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("find-user-live1"));
        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("find-user-live2"));
        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andDo(document("find-user-live3"));
    }

    @Test
    @Transactional
    void modifyProfile() throws Exception {
        mockMvc.perform(put("/pingpong/users/" + user.getIntraId() + "/detail").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user.getId().toString())
                .header("Authorization", "Bearer 0"))
                .andExpect(status().isOk())
                .andDo(document("modify-user-profile"));
    }
}