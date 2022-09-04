package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.Map;

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
class SlotControllerNormalTest {

    @Autowired
    private TestInitiator testInitiator;

    @Autowired
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public Slot[] slots;
    public User[] users;
    public Team[] teams;
    Map<String, String> HttpRequestBody;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
        HttpRequestBody = new HashMap<>();
    }

    // given

    // 일반전에 대한 모드별 나타나는 케이스
    // 유저가 있을 때와 없을 때

    // when

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

    /*
       슬롯에 유저가 잘못 들어간 경우
       ( mode = rank 일 경우 ) 단식인 경우
       빈 슬롯에 유저가 노말로 들어가서 그 슬롯에 들어가서 노말 방을 만든다.
       그 방을 유저가 랭크로 들어가려고 할 때 그 슬롯이 노말이기 때문에 랭크로 못 들어가게 하려는 상황.

     */
    @Test
    @Transactional
    @DisplayName("노말 슬롯에 감히 랭크로 들어온 경우 에러처리")
    void normalAddUserSlotValidType() throws Exception {
        Slot slot = testInitiator.slots[0];

        Map<String, String> body1 = new HashMap<>();
        body1.put("slotId", slot.getId().toString());
        body1.put("mode", Mode.NORMAL.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body1))
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("slot-couldn't-add-user-when-rank-access-normal-room1"));

        Map<String, String> body2 = new HashMap<>();
        body2.put("slotId", slot.getId().toString());
        body2.put("mode", Mode.RANK.getCode());
        mockMvc.perform(post("/pingpong/match/tables/1/{type}", GameType.SINGLE.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body2))
                        .header("Authorization", "Bearer " + testInitiator.tokens[1].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("slot-couldn't-add-user-when-rank-access-normal-room2"));
    }

    @Test
    @Transactional
    @DisplayName("슬롯 조회 (매칭 테이블 조회")
    void normalSlotListSingle() throws Exception {
        // 단식 유저 일반에 대한 리스트 조회
        /*
        유저 2명 (풀방)
        status : close
        */

        /*
        유저1(1000p) -> 접근할 수 없는 점수 차의 슬롯(ppp)
        */

        /*
        유저1 -> 접근할 수 없는 슬롯(mode : rank)
         */

    }

    @Test
    @Transactional
    @DisplayName("일반전 단식 슬롯 등록 (API 미정)")
    void normalSlotAddUserSingle() throws Exception {
        // 빈 칸(0 / 2) 등록
        // 슬롯 확인

        // (1 / 2) - user2 등록
        // 슬롯 확인


    }

    @Test
    @Transactional
    @DisplayName("슬롯 등록 예외 - /match/tables/1/single")
    void slotAddUserException() throws Exception {
        // 단복식에 대한 예외처리.

        /*
         * SlotId = -1 (음수인 경우)
         * -> 400
         * */

        /*
         * SlotId = null (없는 경우)
         * -> 400
         * */


        /*
         * SlotId = string (문자열인 경우)
         * -> 400
         * */

        /*
         * 단식 - 유저(840p) -> Slot(1000p) 접근
         * -> 400
         * */

        /*
         * 단식 - 유저(Single) -> Slot(Duoble) 접근
         * -> 400
         * */

        /*
         * 단식 - 풀방 (2/2) 접근
         * -> 400
         * */


        /*
         * 시간이 지난 슬롯 접근
         * -> 400
         * */
    }

    @Test
    @Transactional
    @DisplayName("일반전 복식 슬롯 등록 (API 미정)")
    void normalSlotAddUserDouble() throws Exception {
        // 추후 구현

    }

    @Test
    @Transactional
    @DisplayName("일반전 슬롯 등록 단식 && 에러 (API 미정)")
    void normalSlotRemoveUserSingle() throws Exception {
        // 단복식에 대한 예외처리.

        /*
         * SlotId = -1 (음수인 경우)
         * -> 400
         * */

        /*
         * SlotId = null (없는 경우)
         * -> 400
         * */


        /*
         * SlotId = string (문자열인 경우)
         * -> 400
         * */

        /*
         * 단식 - 유저(840p) -> Slot(1000p) 접근
         * -> 400
         * */

        /*
         * 단식 - 유저(Single) -> Slot(Duoble) 접근
         * -> 400
         * */

        /*
         * 단식 - 풀방 (2/2) 접근
         * -> 400
         * */


        /*
         * 시간이 지난 슬롯 접근
         * -> 400
         * */
    }

    @Test
    @Transactional
    @DisplayName("일반전 슬롯 등록 복식 && 에러 (API 미정)")
    void normalSlotRemoveUserDouble() throws Exception {
        // 추후 구현

    }
}