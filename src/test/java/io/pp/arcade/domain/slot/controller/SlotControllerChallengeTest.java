package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RealWorld;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.opponent.Opponent;
import io.pp.arcade.v1.domain.opponent.OpponentRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.Mode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SlotControllerChallengeTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RealWorld realWorld;
    @Autowired
    OpponentRepository opponentRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    SlotTeamUserRepository slotTeamUserRepository;
    @Autowired
    CurrentMatchRepository currentMatchRepository;
    @Autowired
    SeasonRepository seasonRepository;

    User[] guestUsers;
    User[] adminUsers;
    Opponent[] opponents;


    @BeforeAll
    void init() {
        guestUsers = realWorld.getGuestUsers();
        adminUsers = realWorld.getAdminUsers();
        opponents = new Opponent[10];
        for (int i = 0; i < 10; i++) {
            opponents[i] = opponentRepository.save(new Opponent(adminUsers[i].getIntraId(), "nick" + i, "", "hihi", i % 3 != 0));
        }
        seasonRepository.save(Season.builder()
                        .seasonMode(Mode.BOTH)
                        .startTime(LocalDateTime.now().minusDays(1))
                        .endTime(LocalDateTime.now().plusDays(1))
                        .seasonName("softwave")
                        .startPpp(1000)
                        .pppGap(500)
                .build());
    }

    @DisplayName("챌린지모드_슬롯입장")
    @Nested
    class ChallengeModeSlotAddUser {
        @Test
        void 정상입장() throws Exception {
            //given
            Slot slot = realWorld.getEmptySlotMinutesLater(10);
            User enteringUser = guestUsers[0];
            User opponent = adminUsers[0];
            //when
            Map<String, String> firstPost = new HashMap<>();
            firstPost.put("slotId", String.valueOf(slot.getId()));
            firstPost.put("mode", "challenge");
            firstPost.put("opponent", null);
            mockMvc.perform(post("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(firstPost))
                    .header("Authorization", "Bearer " + tokenRepository.findByUserId(enteringUser.getId()).orElseThrow().getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("slot-add-user-in-challenge-mode-1/2"));
            //then
            Slot slotAfterEntering = slotRepository.findById(slot.getId()).orElseThrow();
            SlotTeamUser enteringUserSTU = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slot.getId(), enteringUser.getId()).orElseThrow();
            CurrentMatch enteringUserCurrentMatch = currentMatchRepository.findByUserAndIsDel(enteringUser, false).orElseThrow();
            Assertions.assertThat(slotAfterEntering.getHeadCount()).isEqualTo(1);
            Assertions.assertThat(enteringUserSTU.getUser().getId()).isEqualTo(enteringUser.getId());
            Assertions.assertThat(enteringUserCurrentMatch.getSlot().getId()).isEqualTo(slot.getId());

            //when
            Map<String, String> secondPost = new HashMap<>();
            secondPost.put("slotId", String.valueOf(slot.getId()));
            secondPost.put("mode", "challenge");
            secondPost.put("opponent", opponent.getIntraId());
            mockMvc.perform(post("/pingpong/match/tables/1/single").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(secondPost))
                            .header("Authorization", "Bearer " + tokenRepository.findByUserId(enteringUser.getId()).orElseThrow().getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("slot-add-user-in-challenge-mode-2/2"));
            //then
            Slot slotAfterOpponentEntering = slotRepository.findById(slot.getId()).orElseThrow();
            SlotTeamUser enteringOpponentSTU = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slot.getId(), opponent.getId()).orElseThrow();
            CurrentMatch opponentCurrentMatch = currentMatchRepository.findByUserAndIsDel(opponent, false).orElse(null);
            Assertions.assertThat(slotAfterOpponentEntering.getHeadCount()).isEqualTo(2);
            Assertions.assertThat(enteringOpponentSTU.getUser().getId()).isEqualTo(opponent.getId());
            Assertions.assertThat(opponentCurrentMatch).isNull();
        }

        @Test
        void 비정상입장() {

        }
    }

    @DisplayName("챌린지모드_슬롯퇴장")
    @Nested
    class ChallengeModeSlotRemoveUser {
        @Test
        void 정상퇴장() throws Exception {
            //given
            User guest = guestUsers[0];
            User admin = adminUsers[0];
            Slot slot = realWorld.getChallengeSlotWithTwoUsersMinutesLater(guest, admin, 10);
            //when
            mockMvc.perform(delete("/pingpong/match/slots/{slotId}", slot.getId()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + tokenRepository.findByUserId(guest.getId()).orElseThrow().getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("slot-remove-user-in-challenge-mode-2/2"));
            //then
            Slot slotAfterLeaving = slotRepository.findById(slot.getId()).orElseThrow();
            SlotTeamUser guestSTU = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slot.getId(), guest.getId()).orElse(null);
            SlotTeamUser adminSTU = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slot.getId(), admin.getId()).orElse(null);
            CurrentMatch guestCurrentMatch = currentMatchRepository.findByUserAndIsDel(guest, false).orElse(null);
            CurrentMatch adminCurrentMatch = currentMatchRepository.findByUserAndIsDel(admin, false).orElse(null);
            Assertions.assertThat(slotAfterLeaving.getHeadCount()).isEqualTo(0);
            Assertions.assertThat(slotAfterLeaving.getMode()).isEqualTo(Mode.BOTH);
            Assertions.assertThat(slotAfterLeaving.getGamePpp()).isEqualTo(null);
            Assertions.assertThat(guestSTU).isNull();
            Assertions.assertThat(adminSTU).isNull();
            Assertions.assertThat(guestCurrentMatch).isNull();
            Assertions.assertThat(adminCurrentMatch).isNull();
        }

        @Test
        void 비정상퇴장() {

        }
    }


}
