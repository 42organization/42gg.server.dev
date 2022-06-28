package io.pp.arcade.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.season.Season;
import io.pp.arcade.domain.season.SeasonRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import io.pp.arcade.global.util.EloRating;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class GameAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TestInitiator testInitiator;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private PChangeRepository pChangeRepository;

    EloRating eloRating;

    User user1;
    User user2;
    User user3;
    User user4;
    Team team1;
    Team team2;
    Team team3;
    Team team4;
    Slot slot1;
    Slot slot2;
    Game game1;
    Season season;

    /* gameUpdate를 위한 */
    User user5;
    User user6;
    Team team5;
    Team team6;
    Slot slot3;
    Game beforeUpdateGame;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        user1 = testInitiator.users[0];
        user2 = testInitiator.users[1];
        user3 = testInitiator.users[2];
        user4 = testInitiator.users[3];
        team1 = testInitiator.teams[0];
        team2 = testInitiator.teams[1];
        team3 = testInitiator.teams[2];
        team4 = testInitiator.teams[3];
        slot1 = testInitiator.slots[0];
        slot1.setType(GameType.SINGLE);
        slot2 = testInitiator.slots[1];
        slot2.setType(GameType.SINGLE);
        season = seasonRepository.save(Season.builder()
                .seasonName("7기")
                .startTime(LocalDateTime.of(2022, 7, 4, 15, 00))
                .endTime(LocalDateTime.of(2023, 1, 4, 15, 00))
                .startPpp(100)
                .pppGap(50)
                .build());

        game1 = gameRepository.save(Game.builder()
                .slot(slot1)
                .team1(slot1.getTeam1())
                .team2(slot1.getTeam2())
                .type(slot1.getType())
                .time(slot1.getTime())
                .season(season.getId())
                .status(StatusType.WAIT)
                .build());

        /* gameUpdate를 위한 */
        /* team5(user5)가 2 team6(user6)가 1로 게임 이겼는데 스코어 반대로 바꿀거임 */
        user5 = testInitiator.users[4];
        user5.setPpp(50);
        team5 = testInitiator.teams[4];
        team5.setUser1(user5);
        team5.setUser2(null);
        team5.setHeadCount(1);
        team5.setScore(2);
        team5.setWin(true);
        team5.setTeamPpp(user5.getPpp());
        user6 = testInitiator.users[5];
        user6.setPpp(70);
        team6 = testInitiator.teams[5];
        team6.setUser1(user6);
        team6.setUser2(null);
        team6.setHeadCount(1);
        team6.setScore(1);
        team6.setWin(false);
        team6.setTeamPpp(user6.getPpp());

        slot3 = slotRepository.save(Slot.builder()
                        .tableId(1)
                .team1(team5)
                .team2(team6)
                .gamePpp(team5.getTeamPpp())
                .headCount(2)
                .time(LocalDateTime.of(2022, 6, 27, 15,00))
                .build());
        beforeUpdateGame = gameRepository.save(Game.builder()
                .slot(slot3)
                .team1(slot3.getTeam1())
                .team2(slot3.getTeam2())
                .time(slot3.getTime())
                .type(GameType.SINGLE)
                .status(StatusType.END)
                .season(1)
                .build());
        /* user5의 pChange */
        PChange beforeUpdatePChangeUser5 = pChangeRepository.save(PChange.builder()
                .game(beforeUpdateGame)
                .user(user5)
                .pppChange(EloRating.pppChange(user5.getPpp(), user6.getPpp(), true))
                .pppResult(user5.getPpp() + EloRating.pppChange(user5.getPpp(), user6.getPpp(), true))
                .build());
        PChange beforeUpdatePChangeUser6 = pChangeRepository.save(PChange.builder()
                .game(beforeUpdateGame)
                .user(user6)
                .pppChange(EloRating.pppChange(user6.getPpp(), user5.getPpp(), false))
                .pppResult(user6.getPpp() + EloRating.pppChange(user6.getPpp(), user5.getPpp(), false))
                .build());
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("slotId", slot2.getId().toString());
        body.put("seasonId", season.getId().toString());
        body.put("status", StatusType.END.toString());

        mockMvc.perform(post("/admin/game").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-game-create"));

        Game createdGame = gameRepository.findBySlot(slot2).orElseThrow(null);
        Assertions.assertThat(createdGame.getType()).isEqualTo(slot2.getType());
        Assertions.assertThat(createdGame.getStatus()).isEqualTo(StatusType.END);
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("gameId", beforeUpdateGame.getId().toString());
        body.put("team1Id", beforeUpdateGame.getTeam1().getId().toString());
        body.put("team2Id", beforeUpdateGame.getTeam2().getId().toString());
        body.put("team1Score", "1");
        body.put("team2Score", "2");

        mockMvc.perform(put("/admin/game").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("admin-game-update"));

        Game afterUpdateGame = gameRepository.findBySlot(slot3).orElseThrow(null);
        Assertions.assertThat(afterUpdateGame.getTeam1().getUser1().getPpp()).isLessThan(50);
        Assertions.assertThat(afterUpdateGame.getTeam2().getUser1().getPpp()).isGreaterThan(70);
        PChange afterUpdatePChange = pChangeRepository.findByUserAndGame(user5, afterUpdateGame).orElseThrow(null);
        Assertions.assertThat(afterUpdatePChange.getPppResult()).isEqualTo(afterUpdateGame.getTeam1().getUser1().getPpp());
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        mockMvc.perform(delete("/admin/game/" + game1.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-game-delete"));
        int size = gameRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }

    @Test
    @Transactional
    public void findGame() throws Exception {
        mockMvc.perform(get("/admin/game/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-game-find-all"));

        int size = gameRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }
}
