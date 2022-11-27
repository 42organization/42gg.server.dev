package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.noti.NotiRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RecordApplicationEvents
class   SlotControllerNormalTest {

    @Autowired
    private TestInitiator testInitiator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private SlotTeamUserRepository slotTeamUserRepository;
    @Autowired
    private NotiRepository notiRepository;
    @Autowired
    private MockMvc mockMvc;

    Slot[] slots;
    User[] users;
    Team[] teams;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        teams = testInitiator.teams;
        users = testInitiator.users;
        slots = testInitiator.slots;
    }

    // given

    // 일반전에 대한 모드별 나타나는 케이스
    // 유저가 있을 때와 없을 때
    @Transactional
    Slot saveSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    @Transactional
    void saveSlot(Slot slot, Integer headcount, GameType type, Integer gamePpp, Mode mode) {
        slot.setHeadCount(headcount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
        slot.setMode(mode);
    }
    @Transactional
    void saveSlot(Slot slot, Integer headCount, GameType type, Integer gamePpp, User user, Mode mode) {
        slot.setHeadCount(headCount);
        slot.setType(type);
        slot.setGamePpp(gamePpp);
        slot.setMode(mode);
        if (user != null) {
            currentMatchRepository.save(CurrentMatch.builder().slot(slot).game(null).user(user).matchImminent(false).isMatched(false).build());
        }
    }

    @Transactional
    void saveUserPpp(User user, Integer ppp) {
        user.setPpp(ppp);
    }

    @Transactional
    void saveCurrentMatchImminent(CurrentMatch match, Boolean isImminent) {
        match.setMatchImminent(isImminent);
    }

    @Transactional
    void saveCurrentMatchGame(CurrentMatch match, Slot slot) {
        match.setGame(gameRepository.save(Game.builder().slot(slot).season(1).status(StatusType.LIVE).mode(slot.getMode()).build()));
    }

    @Test
    @Transactional
    @DisplayName("슬롯 조회 (매칭 테이블 조회) - /pingpong/match/tables/1/normal/single")
    void normalSlotListSingle() throws Exception {
        // 단식 유저 일반에 대한 리스트 조회
        /*
        유저 2명 (풀방)
        status : close
        */
        Slot slot = slots[1];
        saveSlot(slot, 2, GameType.SINGLE, 950, (User) null, Mode.NORMAL);
        mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.matchBoards[0][1].status").value(SlotStatusType.CLOSE.getCode()))
                .andDo(document("NORMAL-slot-status-list-when-singleSlot-is-full"));

        /*
        유저1(1000p) -> 접근할 수 없는 점수 차의 슬롯(ppp)이지만 일반전이라 open
        */
        slot = slots[2];
        saveSlot(slot, 1, GameType.SINGLE, 900, (User) null, Mode.NORMAL);
        saveUserPpp(users[10], 100);
        mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[10].getAccessToken()))
                .andExpect(jsonPath("$.matchBoards[0][2].status").value(SlotStatusType.OPEN.getCode()))
                .andDo(document("NORMAL-slot-status-list-after-enter-100p-in-900p"));
        /*
        유저1 -> 접근할 수 없는 슬롯(mode : rank)
         */
        slot = slots[3];
        saveSlot(slot, 2, GameType.SINGLE, 950, null, Mode.RANK);
        mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 10"))
                .andExpect(jsonPath("$.matchBoards[0][3].status").value(SlotStatusType.CLOSE.getCode()))
                .andDo(document("NORMAL-different-mode-slots"));

    }

    @Test
    @Transactional
    @DisplayName("일반전 단식 슬롯 등록 - /match/tables/1/normal/single")
    void normalSlotAddUserSingle() throws Exception {
        Map<String, String> body = new HashMap<>();
        Slot slot;
        User user;
        Slot passedSlot;

        // 빈 칸(0 / 2) 등록
        {
            passedSlot = slots[0];
            user = users[0];
            body.put("slotId", passedSlot.getId().toString());
            body.put("mode", Mode.NORMAL.toString());

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.OPEN.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-before-add-user-in-0(2)-check-is-myTable-or-not"));

            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-add-user-in-0(2)"));

            // 슬롯 확인
            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[11].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.OPEN.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-add-user-in-0(2)-check-other-user-view"));
            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.MYTABLE.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-after-add-user-in-0(2)-check-is-myTable-or-not"));
            slot = slotRepository.getById(passedSlot.getId());
            Assertions.assertThat(slot.getGamePpp()).isEqualTo(user.getPpp());
            Assertions.assertThat(slot.getHeadCount()).isEqualTo(1);
            Assertions.assertThat(slot.getType()).isEqualTo(GameType.SINGLE);
            SlotTeamUser slotTeamUser = slotTeamUserRepository.findSlotTeamUserBySlotIdAndUserId(slot.getId(), user.getId()).orElse(null);
            Team team1 = slotTeamUser.getTeam();
            Assertions.assertThat(slotTeamUser.getUser().getIntraId()).isEqualTo(user.getIntraId());
            Assertions.assertThat(slotTeamUser.getTeam().getTeamPpp()).isEqualTo(user.getPpp());
            Assertions.assertThat(team1.getHeadCount()).isEqualTo(1);
            CurrentMatch user1CurrentMatch = currentMatchRepository.findByUserAndIsDel(user, false).orElse(null);
            Assertions.assertThat(user1CurrentMatch.getSlot().getId()).isEqualTo(slot.getId());
            Assertions.assertThat(user1CurrentMatch.getIsMatched()).isEqualTo(false);
        }

        // (1 / 2) - user2 등록
        {
            slot = slots[0];
            user = users[1];
            Map<String, String> body1 = new HashMap<>();
            body.put("slotId", slot.getId().toString());
            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.OPEN.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-before-add-user-in-1(2)-check-is-myTable-or-not"));

            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-add-user-in-1(2)"));

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.MYTABLE.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-after-add-user-in-1(2)-check-is-myTable-or-not"));

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[11].getAccessToken()))
                    .andExpect(jsonPath("$.matchBoards[0][0].status").value(SlotStatusType.CLOSE.getCode()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-slot-after-add-user-in-1(2)-check-other-user-view"));

            // 슬롯 확인
            List<SlotTeamUser> slotTeamUser = slotTeamUserRepository.findAllBySlotId(slot.getId());
            slot = slotTeamUser.get(0).getSlot();
            Assertions.assertThat(slot.getGamePpp()).isEqualTo((users[0].getPpp() + user.getPpp()) / 2);
            Assertions.assertThat(slot.getHeadCount()).isEqualTo(2);
            Assertions.assertThat(slot.getType()).isEqualTo(GameType.SINGLE);

            Team team1 = slotTeamUser.get(0).getTeam();
            Assertions.assertThat(team1.getTeamPpp()).isEqualTo(users[0].getPpp());
            Assertions.assertThat(team1.getHeadCount()).isEqualTo(1);

            Team team2 = slotTeamUser.get((slotTeamUser.size() + 1) / 2).getTeam();
            Assertions.assertThat(team2.getTeamPpp()).isEqualTo(user.getPpp());
            Assertions.assertThat(team2.getHeadCount()).isEqualTo(1);

            CurrentMatch user1CurrentMatch = currentMatchRepository.findByUserAndIsDel(slotTeamUser.get(0).getUser(), false).orElse(null);
            CurrentMatch user2CurrentMatch = currentMatchRepository.findByUserAndIsDel(user, false).orElse(null);
            Assertions.assertThat(user2CurrentMatch.getSlot().getId()).isEqualTo(slot.getId());
            Assertions.assertThat(user1CurrentMatch.getIsMatched()).isEqualTo(true);
            Assertions.assertThat(user2CurrentMatch.getIsMatched()).isEqualTo(true);

            Noti user0Noti = notiRepository.findAllByUser(users[0]).get(0);
            Noti user1Noti = notiRepository.findAllByUser(user).get(0);
            Assertions.assertThat(user0Noti.getType()).isEqualTo(NotiType.MATCHED);
            Assertions.assertThat(user1Noti.getType()).isEqualTo(NotiType.MATCHED);
        }
    }

    @Test
    @Transactional
    @DisplayName("일반전 복식 슬롯 등록 - /match/tables/1/normal/double")
    void normalSlotAddUserDouble() throws Exception {
        // 추후 구현
    }

    @Test
    @Transactional
    @DisplayName("일반전 단식 슬롯 등록 예외 - /match/tables/1/normal/single")
    void slotAddUserException() throws Exception {
        Map<String, String> body = new HashMap<>();
        Slot slot;
        /*
         * SlotId = -1 (음수인 경우)
         * -> 400
         * */
        body = new HashMap<>();
        body.put("slotId", "-1");
        body.put("mode", Mode.NORMAL.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-add-user-4xxError-cause-slotId-is-negative"));

        /*
         * SlotId = null (없는 경우)
         * -> 400
         * */
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-add-user-4xxError-cause-slotId-is-null"));


        /*
         * SlotId = string (문자열인 경우)
         * -> 400
         * */
        body = new HashMap<>();
        body.put("slotId", "String");
        body.put("mode", Mode.NORMAL.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-add-user-4xxError-cause-slotId-is-string"));

        /*
         * 단식 - 유저(840p) -> Slot(1000p) 접근
         * -> 400
         * */
        slot = slots[0];
        saveSlot(slot, 1, GameType.SINGLE, 1000, Mode.NORMAL);
        saveUserPpp(users[0], 840);
        body.put("slotId", slot.getId().toString());
        body.put("mode", Mode.NORMAL.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("NORMAL-slot-add-user-NOT4xxError-cause-slotPpp-is-too-high-but-dont-care"));

        /*
         * 단식 - 유저(Single) -> Slot(Double) 접근
         * -> 400
         * */
        slot = slots[1];
        saveSlot(slot, 1, GameType.DOUBLE, 1000, Mode.NORMAL);
        body = new HashMap<>();
        body.put("slotId", slot.getId().toString());
        body.put("mode", Mode.NORMAL.getCode());
        saveUserPpp(users[1], 1000);
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-add-user-4xxError-cause-single-try-enter-double"));

        /*
         * 단식 - 풀방 (2/2) 접근
         * -> 400
         * */
        slot = slots[2];
        saveSlot(slot, 2, GameType.SINGLE, 1000, Mode.NORMAL);
        saveUserPpp(users[2], 1000);
        body = new HashMap<>();
        body.put("slotId", slot.getId().toString());
        body.put("mode", Mode.RANK.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-add-user-4xxError-cause-try-enter-full"));


        /*
         * 시간이 지난 슬롯 접근
         * -> 400
         * */
        Slot passedSlot;
        body = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime passed = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 1);
        passed.minusDays(1);
        passedSlot = Slot.builder().tableId(1).time(passed).headCount(0).gamePpp(null).type(GameType.SINGLE).build();
        passedSlot = saveSlot(passedSlot);
        body.put("slotId", passedSlot.getId().toString());
        body.put("mode", Mode.RANK.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("NORMAL-slot-status-list-times-past"));
    }

    @Test
    @Transactional
    @DisplayName("일반전 슬롯 해제 단식 - /match/slots/{slotId}")
    void normalSlotRemoveUserSingle() throws Exception {
        HashMap<String, String> body = new HashMap<String, String>();

        /*
         * 단식(1/2) -> User1 취소
         * */
        {
            Slot slot = slots[1];
            User team1User = users[2];
            body.put("slotId", slot.getId().toString());
            body.put("mode", Mode.NORMAL.getCode());

            /* slot - user1 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single1)slot-add-user1st-in-0(2)"));

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single1)slot-before-user1st-cancel-when-status-1(2)-check-is-myTable-or-not"));

            /* slot - user1 등록 취소 */
            mockMvc.perform(delete("/pingpong/match/slots/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single1)slot-user1st-cancel-when-status-1(2)"));

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[2].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single1)slot-after-user1st-cancel-when-status-1(2)-check-is-myTable-or-not"));
            /* User1 매치테이블 조회 */
            CurrentMatch user1CurrentMatch = currentMatchRepository.findByUserAndIsDel(team1User, false).orElse(null);

            Assertions.assertThat(user1CurrentMatch).isEqualTo(null);
            Assertions.assertThat(slot.getGamePpp()).isEqualTo(null);
            Assertions.assertThat(slot.getHeadCount()).isEqualTo(0);
            Assertions.assertThat(slot.getType()).isEqualTo(null);
        }

        flushAll();

        /*
         * 단식(2/2) -> User1 취소
         * */
        {
            Slot slot = slots[2];
            User team1User = users[3];
            User team2User = users[4];
            body.put("slotId", slot.getId().toString());
            /* slot - user1 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[3].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single2)slot-add-user1st-in-0(2)"));
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[4].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single2)slot-add-user2nd-in-1(2)"));

            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[3].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single2)slot-before-user1st-cancel-when-status-2(2)-check-is-myTable-or-not"));
            /* slot - user1 등록 취소 */
            mockMvc.perform(delete("/pingpong/match/slots/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[3].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("slot-user1st-cancel-status-2(2)"));
            mockMvc.perform(get("/pingpong/match/tables/1/normal/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[3].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single2)slot-after-user1st-cancel-when-status-2(2)-check-is-myTable-or-not"));


            CurrentMatch user1CurrentMatch = currentMatchRepository.findByUserAndIsDel(team1User, false).orElse(null);
            CurrentMatch user2CurrentMatch = currentMatchRepository.findByUserAndIsDel(team2User, false).orElse(null);
            Noti user1Noti = notiRepository.findAllByUser(team1User).get(0); // matched(0), canceled(1)
            Noti user2Noti = notiRepository.findAllByUser(team2User).get(1); // matched(0), canceled(1)

            Assertions.assertThat(slot.getGamePpp()).isEqualTo(team2User.getPpp());
            Assertions.assertThat(slot.getHeadCount()).isEqualTo(1);
            Assertions.assertThat(slot.getType()).isEqualTo(GameType.SINGLE);

            Assertions.assertThat(user1CurrentMatch).isEqualTo(null);
            Assertions.assertThat(user2CurrentMatch.getIsMatched()).isEqualTo(false);

            Assertions.assertThat(user1Noti.getType()).isEqualTo(NotiType.MATCHED);
            Assertions.assertThat(user2Noti.getType()).isEqualTo(NotiType.CANCELEDBYMAN);
            flushAll();

        }

        /*
         * 단식(2/2) -> User2 취소
         * */
        {
            Slot slot = slots[3];
            User team1User = users[5];
            User team2User = users[6];
            body.put("slotId", slot.getId().toString());
            /* slot - user 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[5].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single3)slot-add-user1st-in-0(2)"));
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[6].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single3)slot-add-user2nd-in-1(2)"));

            mockMvc.perform(get("/pingpong/match/tables/1/rank/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[6].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single3)slot-before-user2nd-cancel-when-status-2(2)-check-is-myTable-or-not"));
            /* slot - team2user 등록 취소 */
            mockMvc.perform(delete("/pingpong/match/slots/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[6].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single3)slot-user2nd-cancel-when-status-2(2)"));
            mockMvc.perform(get("/pingpong/match/tables/1/rank/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[6].getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document("NORMAL-(single3)slot-after-user2nd-cancel-when-status-2(2)-check-is-myTable-or-not"));
            CurrentMatch user1CurrentMatch = currentMatchRepository.findByUserAndIsDel(team1User, false).orElse(null);
            CurrentMatch user2CurrentMatch = currentMatchRepository.findByUserAndIsDel(team2User, false).orElse(null);
            Noti user1Noti = notiRepository.findAllByUser(team1User).get(1);
            Noti user2Noti = notiRepository.findAllByUser(team2User).get(0);

            Assertions.assertThat(slot.getGamePpp()).isEqualTo(team1User.getPpp());
            Assertions.assertThat(slot.getHeadCount()).isEqualTo(1);
            Assertions.assertThat(slot.getType()).isEqualTo(GameType.SINGLE);
            Assertions.assertThat(user1CurrentMatch.getIsMatched()).isEqualTo(false);
            Assertions.assertThat(user2CurrentMatch).isEqualTo(null);
            Assertions.assertThat(user1Noti.getType()).isEqualTo(NotiType.CANCELEDBYMAN);
            Assertions.assertThat(user2Noti.getType()).isEqualTo(NotiType.MATCHED);
            flushAll();
        }
        flushAll();
    }

    @Test
    @Transactional
    @DisplayName("일반전 슬롯 해제 복식 - /match/slots/{slotId}")
    void normalSlotRemoveUserDouble() throws Exception {
        // 추후 구현
    }

    @Test
    @Transactional
    @DisplayName("일반전 슬롯 해제 단식 예외 - /match/slots/{slotId}")
    void normalSlotRemoveUserException() throws Exception {
        HashMap<String, String> body = new HashMap<String, String>();
        /*
         * 인원이 빈 슬롯에 취소를 요청할 경우
         * -> 400
         * */
        mockMvc.perform(delete("/pingpong/match/slots/" + slots[0].getId().toString()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                .andExpect(status().isBadRequest());

        flushAll();

        /*
         * currentMatch imminent인 경우
         * -> 400
         * */
        {
            Slot slot = slots[0];
            User team1User = users[0];
            body.put("slotId", slot.getId().toString());
            body.put("mode", Mode.NORMAL.getCode());

            /* slot - user1 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                    .andExpect(status().isOk());

            /* slot - user2 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                    .andExpect(status().isOk());

            /* match - Imminent 등록 */
            CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(team1User, false).orElse(null);
            Assertions.assertThat(currentMatch).isNotEqualTo(null);
            saveCurrentMatchImminent(currentMatch, true);

            /* slot - user1 등록 취소 */
            mockMvc.perform(delete("/pingpong/match/slots/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                    .andExpect(status().isBadRequest())
                    .andDo(document("slot-cancel-imminent-game"));
        }

        flushAll();

        /*
         *  Game 중인 Slot 등록 취소할 경우
         * -> 400
         * */
        {
            Slot slot = slots[6];
            User team1User = users[7];
            User team2User = users[8];
            body.put("slotId", slot.getId().toString());
            body.put("mode", Mode.NORMAL.getCode());
            /* slot - user1 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[7].getAccessToken()))
                    .andExpect(status().isOk());

            /* slot - user2 등록 */
            mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                            .header("Authorization", "Bearer " + testInitiator.tokens[8].getAccessToken()))
                    .andExpect(status().isOk());

            /* match - Imminent 등록 */
            CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(team1User, false).orElse(null);
            CurrentMatch currentMatch2 = currentMatchRepository.findByUserAndIsDel(team2User, false).orElse(null);
            Assertions.assertThat(currentMatch).isNotNull();
            Assertions.assertThat(currentMatch2).isNotNull();
            saveCurrentMatchImminent(currentMatch, true);
            saveCurrentMatchImminent(currentMatch2, true);
            saveCurrentMatchGame(currentMatch, slot);
            saveCurrentMatchGame(currentMatch2, slot);

            /* slot - user1 등록 취소 imminent라서 취소 불가*/
            mockMvc.perform(delete("/pingpong/match/slots/" + slot.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testInitiator.tokens[7].getAccessToken()))
                    .andExpect(status().isBadRequest())
                    .andDo(document("slot-cancel-live-game"));
        }

        flushAll();

    }

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://"+ host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}