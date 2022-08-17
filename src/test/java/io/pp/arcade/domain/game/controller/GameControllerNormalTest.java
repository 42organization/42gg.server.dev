package io.pp.arcade.domain.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.currentmatch.CurrentMatch;
import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class GameControllerNormalTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {

    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 전체 (/games)")
    void gameResultAll() throws  Exception {

        /*
         * gameId != Integer (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gameId", "string");
        params.add("count", "20");
        params.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-all-results-4XXError-cause-gameId-is-not-Integer"));

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("game-find-all-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-all-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-all-results-find-id-1000"));
    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 랭크 (/games/rank)")
    void gameResultRankOnly() throws Exception {
        /*
         * gameId != Integer (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gameId", "string");
        params.add("count", "20");
        params.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-rank-results-4XXError-cause-gameId-is-not-Integer"));

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-rank-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-find-id-1000"));
    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 노말 (/games/normal)")
    void gameResultNormalOnly() throws Exception {
        /*
         * gameId != Integer (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gameId", "string");
        params.add("count", "20");
        params.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-rank-results-4XXError-cause-gameId-is-not-Integer"));

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("game-find-rank-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(100))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("game-find-rank-results-find-id-1000"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 전체 (/games)")
    void gameResultByUserIdAndIndexAndCountAll() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games","NOTFOUND").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("user-game-find-all-results-4xxError-cause-couldn't-find-intraId"));

        /*
         * 사용자 - 경기기록이 없는 경우
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("gameId", "1234");
        params3.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0")
                .andExpect(jsonPath("$.games").isEmpty())
                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("user-game-find-all-results-theres-no-game-record"));

        /*
         * 사용자 - 랭크, 노말 다 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "1234");
        params4.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer 0")
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("user-game-find-all-results"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 랭크 (/games/rank)")
    void gameResultByUserIdAndIndexAndCountRankOnly() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/rank","NOTFOUND").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("user-game-find-rank-results-4xxError-cause-couldn't-find-intraId"));

        /*
         * 사용자 - 경기기록이 없는 경우
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("gameId", "1234");
        params3.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/rank", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params3)
                .header("Authorization", "Bearer 0")
                .andExpect(jsonPath("$.games").isEmpty())
                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("user-game-find-rank-results-theres-no-game-record"));

        /*
         * 사용자 - 랭크만 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "1234");
        params4.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/rank", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer 0")
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("user-game-find-rank-results"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 노말 (/games/normal)")
    void gameResultByUserIdAndIndexAndCountNormalOnly() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/normal","NOTFOUND").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("user-game-find-all-results-4xxError-cause-couldn't-find-intraId"));

        /*
         * 사용자 - 경기기록이 없는 경우
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("gameId", "1234");
        params3.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/normal", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params3)
                .header("Authorization", "Bearer 0")
                .andExpect(jsonPath("$.games").isEmpty())
                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("user-game-find-all-results-theres-no-game-record"));

        /*
         * 사용자 - 노말만 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "1234");
        params4.add("count", "20");
        mockMvc.perform(get("/pingpong/users/{intraId}/games/normal", users[2].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer 0")
                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("user-game-find-normal-results"));
    }

}