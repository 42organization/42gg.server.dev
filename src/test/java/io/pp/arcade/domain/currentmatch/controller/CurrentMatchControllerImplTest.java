package io.pp.arcade.domain.currentmatch.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class CurrentMatchControllerImplTest {

    @Autowired
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private GameRepository gameRepository;

    User user;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
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
        user6 = User.builder().intraId("new").statusMessage("").ppp(1000).build();
        users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
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
    public void currentMatchSave(Game game, Slot slot, User user, boolean m, boolean ism) {
        currentMatchRepository.save(CurrentMatch.builder()
                .user(user)
                .slot(slot)
                .game(game)
                .matchImminent(m)
                .isMatched(ism)
                .build());
    }

    @Transactional
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    ;

    @Transactional
    public Slot saveSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }


    @Test
    @Transactional
    void currentMatchFind() throws Exception {
        /*
         * 현재 유저 매칭 테이블 탐색
         * - 해당 유저가 예약된 경기가 없을 경우
         * - 해당 유저가 예약된 경기가 있으며 5분전이 아닐 경우
         * - 해당 유저가 예약된 경기가 있으며 5분전일 때, 매치가 성사되지 않을 경우
         * - 해당 유저가 예약된 경기가 있으며 5분전일 때, 매치가 성사된 경우
         * - 해당 유저가 예약된 경기가 있으며 경기가 시작된 경우
         * */

        // 해당 유저가 예약된 경기가 없을 경우
        // 유저 : user
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-info-none"));

        // 해당 유저가 예약된 경기가 있으며 5분전이 아닐 경우
        // 유저 : user1
        // 슬롯 : slotList.get(0)
        currentMatchSave(null, slotList.get(0), user1, false, false);
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-info-standby-not-Imminent"));

        // 해당 유저가 예약된 경기가 있으며 5분전일 때, 매치가 성사되지 않을 경우
        // 유저 : user2
        // 슬롯 : slotList.get(1)
        currentMatchSave(null, slotList.get(1), user2, true, false);
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user2.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-info-standby-Imminent"));

        // 해당 유저가 예약된 경기가 있으며 5분전일 때, 매치가 성사된 경우
        // 유저 : user3
        // 슬롯 : slot
        Team team1 = Team.builder().teamPpp(100).headCount(2).user1(user3).user2(user4).score(0).build();
        Team team2 = Team.builder().teamPpp(100).headCount(2).user1(user5).user2(user6).score(0).build();
        saveTeam(team1);
        saveTeam(team2);
        Slot slot = Slot.builder().tableId(1).team1(team1).team2(team2).headCount(4).time(LocalDateTime.now().plusDays(1)).build();
        slot = saveSlot(slot);

        currentMatchSave(null, slot, user3, true, true);
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user3.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-info-matching-Imminent"));

        // 해당 유저가 예약된 경기가 있으며 경기가 시작된 경우
        // 유저 : user4
        // 슬롯 : slot
        team1 = Team.builder().teamPpp(100).headCount(2).user1(user3).user2(user4).score(0).build();
        team2 = Team.builder().teamPpp(100).headCount(2).user1(user5).user2(user6).score(0).build();
        saveTeam(team1);
        saveTeam(team2);
        slot = Slot.builder().tableId(1).team1(team1).team2(team2).headCount(4).time(LocalDateTime.now().plusDays(1)).build();
        slot = saveSlot(slot);
        Game game = Game.builder().team1(team1).team2(team2).type("double").season(1).slot(slot).time(slot.getTime()).status("live").build();
        game = saveGame(game);
        currentMatchSave(game, slot, user4, true, true);
        mockMvc.perform(get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", user4.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("current-match-info-gaming"));
    }
}