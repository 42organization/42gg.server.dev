package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.util.RacketType;
import net.bytebuddy.implementation.bytecode.Throw;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class UserControllerTest {
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
    private MockMvc mockMvc;

    User user;
    User user2;
    User user3;
    User user4;

    Game game;
    Team team1;
    Team team2;

    @BeforeEach
    void init() {
        user = userRepository.save(User.builder().intraId("jiyun1").statusMessage("").ppp(42).build());
        user2 = userRepository.save(User.builder().intraId("jiyun2").statusMessage("").ppp(24).build());
        user3 = userRepository.save(User.builder().intraId("nheo1").statusMessage("").ppp(60).build());
        user4 = userRepository.save(User.builder().intraId("nheo2").statusMessage("").ppp(30).build());
        userRepository.save(User.builder()
                .intraId("hakim")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("nheo")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("donghyuk")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("wochae")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("jekim")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("jihyukim")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("jabae")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("kipark")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("daekim")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("sujpark")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        userRepository.save(User.builder()
                .intraId("Polarbear")
                .statusMessage("")
                .ppp(1)
                .build()
        );
        PChange pChange;
        for (int i = 0; i < 10; i++) {
            Team team1 = teamRepository.save(Team.builder().teamPpp(0)
                    .user1(user).headCount(1).score(0).build());
            Team team2 = teamRepository.save(Team.builder().teamPpp(0)
                    .user1(user2).headCount(1).score(0).build());
            Slot slot = slotRepository.save(Slot.builder()
                    .team1(team1)
                    .team2(team2)
                    .headCount(2)
                    .type("single")
                    .tableId(1)
                    .time(LocalDateTime.now().plusDays(1))
                    .gamePpp(50)
                    .build());
            game = gameRepository.save(Game.builder().slot(slot).team1(team1).team2(team2).type(slot.getType()).time(slot.getTime()).season(1).status("end").build());

            pChange = pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(user)
                    .pppChange(2)
                    .pppResult(2 + user.getPpp())
                    .build());
        }
    }

    @Test
    @Transactional
    void findUser() throws Exception {
        mockMvc.perform(get("/pingpong/users").contentType(MediaType.APPLICATION_JSON)
                .param("userId",user.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("find-user"));
    }

    @Test
    @Transactional
    void findDetailUser() throws Exception {
        mockMvc.perform(get("/pingpong/users/"+ user.getIntraId().toString() +"/detail").contentType(MediaType.APPLICATION_JSON)
                .param("currentUserId",user.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("find-user-detail"));
    }

    @Test
    @Transactional
    void findUserHistorics() throws Exception {
        mockMvc.perform(get("/pingpong/users/"+ user.getIntraId().toString() +"/historics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("find-user-historics"));
    }

    @Test
    @Transactional
    void findByPartsOfIntraId() throws Exception {
        mockMvc.perform(get("/pingpong/users/searches").contentType(MediaType.APPLICATION_JSON)
                .param("userId", "zzang"))
                .andExpect(status().isOk())
                .andDo(document("search-user-with-partial-string"));
    }
}