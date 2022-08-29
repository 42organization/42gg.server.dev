package io.pp.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.noti.NotiRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.type.NotiType;
import io.pp.arcade.global.type.StatusType;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        testInitiator.letsgo();
    }

    // given

    // 일반전에 대한 모드별 나타나는 케이스
    // 유저가 있을 때와 없을 때
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