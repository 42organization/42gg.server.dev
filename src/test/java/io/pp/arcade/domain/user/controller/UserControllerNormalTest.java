package io.pp.arcade.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.security.jwt.TokenRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.RacketType;
import io.pp.arcade.global.type.StatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class UserControllerNormalTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {

    }

    @Test
    @Transactional
    @DisplayName("유저 정보 조회 - 상세히, 레벨(공통) (/users/{targetIntraId}/detail)")
    void findDetailUserNormal() throws Exception {
        mockMvc.perform(get("/pingpong/users/targetIntraId/detail").contentType(MediaType.APPLICATION_JSON) // intra id 넣어야함
                .header() // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userImageUri").equals()) // 이미지 주소
                .andExpect(jsonPath("$.racketType").equals()) // 라켓타입
                .andExpect(jsonPath("$.statusMessage").equals()) // 상태메시지
                .andExpect(jsonPath("$.level").equals()) // 레벨
                .andExpect(jsonPath("$.currentExp").equals()) // 현재 경험치
                .andExpect(jsonPath("$.maxExp").equals()) // 현재 레벨 최대 경험치
                .andDo(document("user-profile-detail")));
    }

    @Test
    @Transactional
    @DisplayName("유저 정보 조회 - 상세히, 랭크 (/pingpong/users/{targetIntraId}/rank/{season})")
    void findDetailUserRank() throws Exception {
        mockMvc.perform(get("/pingpong/users/targetIntraId/rank/season").contentType(MediaType.APPLICATION_JSON) // intra id, season 값 넣어야함
                .header() // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rank").equals()) // 등수
                .andExpect(jsonPath("$.ppp").equals()) // 탁구력
                .andExpect(jsonPath("$.wins").equals()) // 승리
                .andExpect(jsonPath("$.losses").equals()) // 패배
                .andExpect(jsonPath("$.winRate").equals()) // 승률
                .andDo(document("user-profile-rank")));
    }

    @Test
    @Transactional
    @DisplayName("유저 실시간 정보 - mode 추가 (/users/live)")
    void userLiveInfo() throws Exception {

        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                        .header() // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.event").isEmpty())
                .andExpect(jsonPath("$.mode").equals()) // enum 추가
                .andDo(document("user-live")));
    }

}