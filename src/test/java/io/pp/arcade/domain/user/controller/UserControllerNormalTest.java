package io.pp.arcade.domain.user.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.rank.dto.RankFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private TestInitiator initiator;

    @Autowired
    private RankRedisService rankRedisService;

    @BeforeEach
    void init() {
        initiator.letsgo();
    }

    @Test
    @Transactional
    @DisplayName("유저 정보 조회 - 상세히, 레벨(공통) (/users/{targetIntraId}/detail)")
    void findDetailUserNormal() throws Exception {
        mockMvc.perform(get("/pingpong/users/{targetIntraId}/detail", initiator.users[0].getIntraId()).contentType(MediaType.APPLICATION_JSON) // intra id 넣어야함
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken())) // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userImageUri").equals()) // 이미지 주소
//                .andExpect(jsonPath("$.racketType").equals()) // 라켓타입
//                .andExpect(jsonPath("$.statusMessage").equals()) // 상태메시지
//                .andExpect(jsonPath("$.level").equals()) // 레벨
//                .andExpect(jsonPath("$.currentExp").equals()) // 현재 경험치
//                .andExpect(jsonPath("$.maxExp").equals()) // 현재 레벨 최대 경험치
                .andDo(document("user-profile-detail"));
    }

    @Test
    @Transactional
    @DisplayName("유저 정보 조회 - 상세히, 랭크 (/pingpong/users/{targetIntraId}/rank)")
    void findDetailUserRank() throws Exception {
        User user = initiator.users[0];
        RankUserDto rank = rankRedisService.findRankById(RankFindDto.builder().intraId(user.getIntraId()).gameType(GameType.SINGLE).build());

        mockMvc.perform(get("/pingpong/users/{targetIntraId}/rank", user.getIntraId()).contentType(MediaType.APPLICATION_JSON) // intra id, season 값 넣어야함
//                .param("seasonId", initiator.testSeason.getId().toString()) // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rank").value(rank.getRank())) // 등수
                .andExpect(jsonPath("$.ppp").value(rank.getPpp())) // 탁구력
                .andExpect(jsonPath("$.wins").value(rank.getWins())) // 승리
                .andExpect(jsonPath("$.losses").value(rank.getLosses())) // 패배
                .andExpect(jsonPath("$.winRate").value(rank.getWinRate())) // 승률
                .andDo(document("user-profile-rank-without-seasonId"));

        mockMvc.perform(get("/pingpong/users/{targetIntraId}/rank", user.getIntraId()).contentType(MediaType.APPLICATION_JSON) // intra id, season 값 넣어야함
                .param("seasonId", initiator.testSeason.getId().toString()) // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rank").value(rank.getRank())) // 등수
                .andExpect(jsonPath("$.ppp").value(rank.getPpp())) // 탁구력
                .andExpect(jsonPath("$.wins").value(rank.getWins())) // 승리
                .andExpect(jsonPath("$.losses").value(rank.getLosses())) // 패배
                .andExpect(jsonPath("$.winRate").value(rank.getWinRate())) // 승률
                .andDo(document("user-profile-rank-with-seasonId"));

    }

    @Test
    @Transactional
    @DisplayName("유저 실시간 정보 - mode 추가 (/users/live)")
    void userLiveInfo() throws Exception {

        mockMvc.perform(get("/pingpong/users/live").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken())) // "Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.event").isEmpty())
//                .andExpect(jsonPath("$.currentMatchMode").equals()) // enum 추가
//                .andExpect(jsonPath("$.seasonMode").equals(Mode.NORMAL)) // enum 추가
                .andDo(document("user-live"));
    }

}