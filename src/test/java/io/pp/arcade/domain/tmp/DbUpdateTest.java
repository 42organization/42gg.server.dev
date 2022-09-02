package io.pp.arcade.domain.tmp;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class DbUpdateTest {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SlotTeamUserRepository slotTeamUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TestInitiator testInitiator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void updateDb() throws Exception {
        testInitiator.letsgo();

        Team team1 = teamRepository.save(Team.builder().score(0).teamPpp(0).headCount(1).user1(testInitiator.users[0]).build());
        Team team2 = teamRepository.save(Team.builder().score(0).teamPpp(0).headCount(1).user1(testInitiator.users[1]).build());
        Slot slot1 = slotRepository.save(Slot.builder().team1(team1).team2(team2)
                .type(GameType.SINGLE)
                .gamePpp(1000)
                .headCount(2)
                .tableId(1)
                .time(LocalDateTime.now())
                .build());
        Team team3 = teamRepository.save(Team.builder().score(0).teamPpp(0).headCount(1).user1(testInitiator.users[2]).build());
        Team team4 = teamRepository.save(Team.builder().score(0).teamPpp(0).headCount(1).user1(testInitiator.users[3]).build());
        Slot slot2 = slotRepository.save(Slot.builder().team1(team3).team2(team4)
                .type(GameType.SINGLE)
                .gamePpp(1000)
                .headCount(2)
                .tableId(1)
                .time(LocalDateTime.now())
                .build());
        gameRepository.save(Game.builder()
                .season(1)
                .slot(slot1)
                .team1(slot1.getTeam1())
                .team2(slot1.getTeam2())
                .time(slot1.getTime())
                .type(slot1.getType())
                .status(StatusType.LIVE).build());
        gameRepository.save(Game.builder()
                .season(1)
                .slot(slot2)
                .team1(slot2.getTeam1())
                .team2(slot2.getTeam2())
                .time(slot2.getTime())
                .type(slot2.getType())
                .status(StatusType.LIVE)
                .build());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/update/db")
                        .header("Authorization", "Bearer " + testInitiator.tokens[5].getAccessToken()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isBadRequest())
                .andDo(org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document("update-db-non-admin"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/update/db")
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andDo(org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document("update-db-admin"));

        List<Slot> slots = slotRepository.findAll();
        for (Slot slot : slots) {
            Assertions.assertThat(slot.getTeam1()).isEqualTo(null);
            Assertions.assertThat(slot.getTeam2()).isEqualTo(null);
            Assertions.assertThat(slot.getTeam1()).isEqualTo(null);
            Assertions.assertThat(slot.getTeam2()).isEqualTo(null);
        }

        List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAll();
        for (SlotTeamUser slotTeamUser : slotTeamUsers) {
            Assertions.assertThat(slotTeamUser.getSlot()).isNotEqualTo(null);
            Assertions.assertThat(slotTeamUser.getTeam()).isNotEqualTo(null);
            Assertions.assertThat(slotTeamUser.getUser()).isNotEqualTo(null);
        }

        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            Assertions.assertThat(game.getTeam1()).isEqualTo(null);
            Assertions.assertThat(game.getTeam2()).isEqualTo(null);
            System.out.println("time : " + " " + game.getTime());
            Assertions.assertThat(game.getTime()).isEqualTo(game.getTime());
            Assertions.assertThat(game.getType()).isEqualTo(null);
            Assertions.assertThat(game.getStatus()).isNotEqualTo(null);
        }
    }
}