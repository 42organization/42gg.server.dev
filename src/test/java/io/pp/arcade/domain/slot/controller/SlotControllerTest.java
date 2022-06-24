package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.security.jwt.TokenRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RecordApplicationEvents
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    TestInitiator testInitiator;

    List<Slot> slotList;
    Slot[] slots;
    User[] users;
    Team[] teams;
    Slot passedSlot;
    Slot laterSlot;
    @BeforeEach
    void init() {
        testInitiator.letsgo();
        teams = testInitiator.teams;
        users = testInitiator.users;
        slots = testInitiator.slots;

        //user1 = userRepository.save(User.builder().intraId("hakim").eMail("hihihoho").imageUri("hakim.jpg").statusMessage("kikikaka").ppp(1040).roleType(RoleType.ADMIN).racketType(RacketType.SHAKEHAND).build());
        //token = tokenRepository.save(new Token(user1, "1", "1"));
    }

    @Transactional
    void saveSlot(Slot slot, Integer headCount, GameType type, Integer gamePpp, User user) {
        slot.setHeadCount(headCount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
        if (user != null) {
            currentMatchRepository.save(CurrentMatch.builder().slot(slot).game(null).user(user).matchImminent(false).isMatched(false).build());
        }
    }

    @Transactional
    void saveUserPpp(User user, Integer ppp){
        user.setPpp(ppp);
    }
    @Transactional
    void saveSlot(Slot slot){
        slotRepository.save(slot);
    }

    @Test
    @Transactional
    @DisplayName("슬롯 조회 - 빈 슬롯")
    void noSlots() throws Exception {
        slotRepository.deleteAll();
        mockMvc.perform(get("/pingpong/match/tables/1/{type}").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(jsonPath("$.slotGroups").isEmpty())
                .andExpect(status().isOk())
                .andDo(document("no-slots"));
    }

    @Test
    @Transactional
    @DisplayName("슬롯 조회 - /match/tables/1/{type}")
    void slotStatusList() throws Exception {
        /*
         * 단식 - 유저 2명 (풀방)
         * status : close
         * */
        Slot slot = slots[1];
        saveSlot(slot, 2, GameType.SINGLE, 950, null);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}",GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[0].slots[1].status").value(SlotStatusType.CLOSE.toString()))
                .andDo(document("slot-status-list-2-after-add-2-user"));

        /*
         * 복식 - 유저 4명 (풀방)
         * status : close
         * */
        slot = slots[3];
        saveSlot(slot, 4, GameType.BUNGLE, 750, null);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}", GameType.BUNGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(jsonPath("$.slotGroups[0].slots[3].status").value(SlotStatusType.CLOSE.toString()))
                .andDo(document("slot-status-list-4-with-type-double"));

        /*
         * 단식 - 유저(100p) -> 슬롯(900p) 접근
         * status : close
         * */
        slot = slots[2];
        saveSlot(slot, 1, GameType.SINGLE, 900, null);
        saveUserPpp(users[10], 100);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}",GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[0].slots[2].status").value(SlotStatusType.CLOSE.toString()))
                .andDo(document("slot-status-list-3-after-enter-100p-in-900p"));

        /*
         * 복식 - 유저(100p) -> 슬롯(900p) 접근
         * status : close
         * */
        slot = slots[4];
        saveSlot(slot, 3, GameType.BUNGLE, 900, null);
        saveUserPpp(users[10], 100);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}", GameType.BUNGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[0].slots[4].status").value(SlotStatusType.CLOSE.toString()))
                .andDo(document("slot-status-list-5-after-enter-100p-in-900p"));

        /*
         * 단식 - 자신의 슬롯인 경우
         * status : close
         * */
        slot = slots[5];
        saveSlot(slot, 1, GameType.BUNGLE, 100, users[10]);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}",GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[0].slots[5].status").value(SlotStatusType.MYTABLE.toString()))
                .andExpect(status().isOk())
                .andDo(document("slot-status-list-6-when-i-have-a-slot"));

        /*
         * SINGLE 조회 시, BUNGLE 슬롯 상태
         * status : close
         * */
        slot = slots[6];
        saveSlot(slot, 1, GameType.BUNGLE, 1000, null);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[1].slots[0].status").value(SlotStatusType.CLOSE.toString()))
                .andExpect(status().isOk());

        /*
         * BUNGLE 조회 시, SINGLE 슬롯 상태
         * status : close
         * */
        slot = slots[7];
        saveSlot(slot, 1, GameType.SINGLE, 1000, null);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}", GameType.BUNGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[1].slots[1].status").value(SlotStatusType.CLOSE.toString()))
                .andExpect(status().isOk());

        /*
         * 시간이 지난 슬롯
         * status : close
         * */
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime passed = LocalDateTime.of(now.getYear(),now.getMonth(), now.getDayOfMonth(), 1, 0);
        passedSlot = Slot.builder().tableId(1).team1(teams[0]).team2(teams[1]).time(passed).headCount(0).gamePpp(null).type(GameType.SINGLE).build();
        saveSlot(passedSlot);
        mockMvc.perform(get("/pingpong/match/tables/1/{type}" ,GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.slotGroups[0].slots[0].status").value(SlotStatusType.CLOSE.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void slotAddUser() throws Exception {
        Map<String, String> body1 = new HashMap<>();
        Slot slotA = slots[0];
        slotA.setType(GameType.SINGLE);
        body1.put("slotId", slotA.getId().toString());
        Map<String, String> body2 = new HashMap<>();
        body2.put("slotId", slotA.getId().toString());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body2))
                        .header("Authorization", "Bearer 5"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .header("Authorization", "Bearer 6"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/pingpong/match/tables/1/" + GameType.SINGLE).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("slot-add-user"));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pingpong/match/current").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("current-match-after-add-match"));

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("after-matched-notification"));

        mockMvc.perform(delete("/pingpong/match").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 5"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/notifications").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 4"))
                .andExpect(status().isOk())
                .andDo(document("after-canceled-notification"));

    }

    @Transactional
    void addUserInTeam(Team team, User user) {
        team.setUser1(user);
    }

    @Test
    @Transactional
    void slotRemoveUser() throws Exception {
        // 슬롯에 유저 두 명 추가했다가, 한 명 제거하기
        Slot slot = slots[3];
        saveSlot(slot, 2, GameType.SINGLE, 1000, users[2]);
        Team team1 = slot.getTeam1();
        addUserInTeam(team1, users[2]);
        mockMvc.perform(delete("/pingpong/match").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pingpong/match/tables/1/" + slot.getType().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 2"))
                .andExpect(status().isOk())
                .andDo(document("remove-user-in-slot"));
    }
}