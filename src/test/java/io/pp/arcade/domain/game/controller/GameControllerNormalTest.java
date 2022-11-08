package io.pp.arcade.domain.game.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.pchange.PChange;
import io.pp.arcade.v1.domain.pchange.PChangeRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
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

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class GameControllerNormalTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CurrentMatchRepository currentMatchRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotTeamUserRepository slotTeamUserRepository;

    @Autowired
    TestInitiator initiator;

    @Autowired
    private TeamRepository teamRepository;

    private final int GAMESIZE = 250;
    @Autowired
    private PChangeRepository pChangeRepository;

    Game liveGame;
    Game waitGame;
    Game doubleGame;
    User[] users;
    Team[] teams;
    Game[] endGames;
    Slot[] slots;

    @BeforeEach
    void init() {
        initiator.letsgo();
        users = initiator.users;
        teams = initiator.teams;
        slots = initiator.slots;
        endGames = new Game[GAMESIZE - 2];

        Slot[] slotList = new Slot[GAMESIZE];

        for (int i = 0; i < GAMESIZE - 2; i++) {
            Mode mode = Mode.RANK;
            if (i % 2 == 0)
                mode = Mode.NORMAL;
            slotList[i] = slotRepository.save(Slot.builder()
                    .time(LocalDateTime.now().plusHours(i))
                    .headCount(2)
                    .tableId(1)
                    .gamePpp(1000)
                    .type(GameType.SINGLE)
                    .mode(mode)
                    .build());
            for (int j = 0; j < 2; j++) {
                Team team = teamRepository.save(Team.builder()
                        .slot(slotList[i])
                        .score(j + 1)
                        .headCount(1)
                        .win(j != 0)
                        .teamPpp(1000)
                        .build());
                slotTeamUserRepository.save(SlotTeamUser.builder()
                        .slot(slotList[i])
                        .team(team)
                        .user(users[j])
                        .build());
            }
        }

        /*
         * endGames : user0, user1
         * doubleGames : user2, 3, 4, 5
         * liveGame : user0, 1
         * waitGame : user6, 7
         * */
        slotList[GAMESIZE - 2] = slotRepository.save(Slot.builder()
                .time(LocalDateTime.now().plusHours(GAMESIZE - 2))
                .headCount(4)
                .tableId(1)
                .gamePpp(1000)
                .type(GameType.DOUBLE)
                .build());
        for (int j = 0; j < 2; j++) {
            Team team = teamRepository.save(Team.builder()
                    .slot(slotList[GAMESIZE - 2])
                    .score(j + 1)
                    .headCount(2)
                    .win(j != 0)
                    .teamPpp(1000)
                    .build());
            slotTeamUserRepository.save(SlotTeamUser.builder()
                    .slot(slotList[GAMESIZE - 2])
                    .team(team)
                    .user(users[2 + j * 2])
                    .build());
            slotTeamUserRepository.save(SlotTeamUser.builder()
                    .slot(slotList[GAMESIZE - 2])
                    .team(team)
                    .user(users[2 + j * 2 + 1])
                    .build());
        }

        slotList[GAMESIZE - 1] = slotRepository.save(Slot.builder()
                .time(LocalDateTime.now().plusHours(GAMESIZE - 1))
                .headCount(2)
                .tableId(1)
                .gamePpp(1000)
                .type(GameType.SINGLE)
                .build());
        for (int i = 0; i < 2; i++) {
            Team team = teamRepository.save(Team.builder()
                    .slot(slotList[GAMESIZE - 1])
                    .score(i + 1)
                    .headCount(1)
                    .win(i != 0)
                    .teamPpp(1000)
                    .build());
            slotTeamUserRepository.save(SlotTeamUser.builder()
                    .slot(slotList[GAMESIZE - 1])
                    .team(team)
                    .user(users[i + 6])
                    .build());
        }

        for (int i = 0; i < GAMESIZE - 3; i++){
            endGames[i] = gameRepository.save(Game.builder()
                    .slot(slotList[i])
                    .mode(slotList[i].getMode())
                    .season(1)
                    .status(StatusType.END)
                    .build());
        }

        // double, wait, live 순으로
        waitGame = gameRepository.save(Game.builder()
                .slot(slotList[GAMESIZE - 3])
                .mode(slotList[GAMESIZE - 3].getMode())
                .season(1)
                .status(StatusType.WAIT)
                .build());
        doubleGame = gameRepository.save(Game.builder()
                .slot(slotList[GAMESIZE - 2])
                .mode(slotList[GAMESIZE - 2].getMode())
                .season(1)
                .status(StatusType.END)
                .build());
        liveGame = gameRepository.save(Game.builder()
                .slot(slotList[GAMESIZE - 1])
                .mode(slotList[GAMESIZE - 1].getMode())
                .season(1)
                .status(StatusType.LIVE)
                .build());

        /* pChange 생성 */
        for (int i = 0; i < GAMESIZE - 3; i++){
            pChangeRepository.save(PChange.builder()
                    .game(endGames[i])
                    .user(users[0])
                    .pppChange(20)
                    .pppResult(1000)
                    .build());
            pChangeRepository.save(PChange.builder()
                    .game(endGames[i])
                    .user(users[1])
                    .pppChange(20)
                    .pppResult(1000)
                    .build());
        }
        pChangeRepository.save(PChange.builder().game(doubleGame).user(users[2]).pppChange(20).pppResult(1000).build());
        pChangeRepository.save(PChange.builder().game(doubleGame).user(users[3]).pppChange(20).pppResult(1000).build());
        pChangeRepository.save(PChange.builder().game(doubleGame).user(users[4]).pppChange(20).pppResult(1000).build());
        pChangeRepository.save(PChange.builder().game(doubleGame).user(users[5]).pppChange(20).pppResult(1000).build());
        /*
         * 게임 결과 정보 조회
         * -> 단식,복식 게임 생성
         * -> matchTable에 단식 복식 유저 등록
         * */
        List<SlotTeamUser> liveUsers = slotTeamUserRepository.findAllBySlotId(slotList[GAMESIZE - 1].getId());
        for (SlotTeamUser liveUser : liveUsers) {
            currentMatchRepository.save(CurrentMatch.builder().user(liveUser.getUser()).slot(liveUser.getSlot()).game(liveGame).build());
        }
        List<SlotTeamUser> doubleGameUsers = slotTeamUserRepository.findAllBySlotId(slotList[GAMESIZE - 2].getId());
        for (SlotTeamUser doubleGameUser : doubleGameUsers) {
            currentMatchRepository.save(CurrentMatch.builder().user(doubleGameUser.getUser()).slot(doubleGameUser.getSlot()).game(doubleGame).build());
        }
        List<SlotTeamUser> waitUsers = slotTeamUserRepository.findAllBySlotId(slotList[GAMESIZE - 3].getId());
        for (SlotTeamUser wiatuser : waitUsers) {
            currentMatchRepository.save(CurrentMatch.builder().user(wiatuser.getUser()).slot(wiatuser.getSlot()).game(waitGame).build());
        }
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
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
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
        params2.add("season", "1");
        params2.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
//                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        params3.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
//                .andExpect(jsonPath().value())
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        params11.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-game-find-all-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        params12.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        params4.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        params5.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(100))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        params6.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        params7.add("season", "1");
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("season", "1");
        params8.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-all-results-find-id-1000"));
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
        params.add("season", "1");
        params.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-game-find-rank-results-4XXError-cause-gameId-is-not-Integer"));

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("season", "1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.getCode());
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        params3.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        params11.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-game-find-rank-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        params12.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        params4.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        params5.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath("$.games.length()").value(100))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        params6.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        params7.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         *
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("status", StatusType.END.getCode());
        params8.add("season", "1");
        mockMvc.perform(get("/pingpong/games/rank").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-rank-results-find-id-1000"));
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
        params.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-game-find-normal-results-4XXError-cause-gameId-is-not-Integer"));

        /*
         * gameId -> -1 (음수인 경우)
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "-1");
        params2.add("count", "20");
        params2.add("status", StatusType.END.getCode());
        params2.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value()) // have to check gameId, mode, ...
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-gameId-is-negative"));

        /*
         * gameId -> null
         * -> 최근 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params3 = new LinkedMultiValueMap<>();
        params3.add("count", "20");
        params3.add("status", StatusType.END.getCode());
        params3.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value())
                .andExpect(jsonPath("$.games.length()").value(20))
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-result-gameId-is-null"));

        /*
         * count -> string (숫자가 아닌 경우)
         * -> RequestDto Binding Error
         * -> 400
         * */
        MultiValueMap<String,String> params11 = new LinkedMultiValueMap<>();
        params11.add("count", "string");
        params11.add("status", StatusType.END.getCode());
        params11.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params11)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-game-find-normal-results-4XXError-cause-count-is-string"));

        /*
         * count -> -1 (음수인 경우)
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params12 = new LinkedMultiValueMap<>();
        params12.add("count", "-1");
        params12.add("status", StatusType.END.getCode());
        params12.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params12)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-count-is-negative"));

        /*
         * count -> null
         * -> 기본 20개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345678");
        params4.add("status", StatusType.END.getCode());
        params4.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params4)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(10))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-count-is-null"));
        /*
         * count -> 1234 (100이상인 경우)
         * -> 100개의 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params5 = new LinkedMultiValueMap<>();
        params5.add("count", "12345678");
        params5.add("status", StatusType.END.getCode());
        params5.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params5)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(100))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-count-is-bigger-than-100"));

        /*
         * status -> NOTHING (다른 값인 경우)
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params6 = new LinkedMultiValueMap<>();
        params6.add("count", "20");
        params6.add("status", "NOTHING");
        params6.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params6)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-user-info-status-wrong"));

        /*
         * status -> null
         * -> live, wait, end 모든 게임정보 반환
         * -> 200
         * */
        MultiValueMap<String,String> params7 = new LinkedMultiValueMap<>();
        params7.add("count", "20");
        params7.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params7)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-user-info-status-is-null"));

        /*
         * GameId -> 1000
         * -> GameId 99번부터 리스트 반환
         * -> 200
         * */
        MultiValueMap<String,String> params8 = new LinkedMultiValueMap<>();
        params8.add("gameId", "12345678");
        params8.add("count", "20");
        params8.add("status", StatusType.END.getCode());
        params8.add("season", "1");
        mockMvc.perform(get("/pingpong/games/normal").contentType(MediaType.APPLICATION_JSON)
                        .params(params8)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games.length()").value(20))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-game-find-normal-results-find-id-1000"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 전체 (/pingpong/games/users/{intraId})")
    void gameResultByUserIdAndIndexAndCountAll() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        params2.add("season", "1");
        mockMvc.perform(get("/pingpong/games/users/{intraId}","NOTFOUND").contentType(MediaType.APPLICATION_JSON)
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
        params3.add("season", "1");
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[10].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                        .params(params3)
                        .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games").isEmpty())
                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("user-game-find-all-results-theres-no-game-record"));

        /*
         * 사용자 - 랭크, 노말 다 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345");
        params4.add("count", "20");
        params4.add("season", "1");
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[0].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("user-game-find-all-results"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 랭크 (/pingpong/games/users/{intraId}?mode=rank)")
    void gameResultByUserIdAndIndexAndCountRankOnly() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        params2.add("season", "1");
        params2.add("mode", Mode.RANK.getCode());
        mockMvc.perform(get("/pingpong/games/users/{intraId}","NOTFOUND").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-user-game-find-rank-results-4xxError-cause-couldn't-find-intraId"));

        /*
         * 사용자 - 경기기록이 없는 경우
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("gameId", "1234");
        params3.add("count", "20");
        params3.add("season", "1");
        params3.add("mode", Mode.RANK.getCode());
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[10].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params3)
                .header("Authorization", "Bearer 0"))
                .andExpect(jsonPath("$.games").isEmpty())
                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("v1-user-game-find-rank-results-theres-no-game-record"));

        /*
         * 사용자 - 랭크만 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345");
        params4.add("count", "20");
        params4.add("season", "1");
        params4.add("mode", Mode.RANK.getCode());
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[0].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer 0"))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-user-game-find-rank-results"));
    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 노말 (/pingpong/games/users/{intraId}?mode=normal)")
    void gameResultByUserIdAndIndexAndCountNormalOnly() throws Exception {
        /*
         * IntraId 찾을 수 없는 경우
         * -> 400
         * */
        LinkedMultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("gameId", "1234");
        params2.add("count", "20");
        params2.add("mode", Mode.NORMAL.getCode());
        params2.add("season", "1");
        mockMvc.perform(get("/pingpong/games/users/{intraId}","notfound").contentType(MediaType.APPLICATION_JSON)
                        .params(params2)
                        .header("Authorization", "Bearer 0"))
                .andExpect(status().isBadRequest())
                .andDo(document("v1-user-game-find-all-results-4xxError-cause-couldn't-find-intraId"));

        /*
         * 사용자 - 경기기록이 없는 경우
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("gameId", "1234");
        params3.add("count", "20");
        params3.add("mode", Mode.NORMAL.getCode());
        params3.add("season", "1");
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[10].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params3)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
                .andExpect(jsonPath("$.games").isEmpty())
//                .andExpect(jsonPath("$.lastGameId").value(0))
                .andExpect(status().isOk())
                .andDo(document("v1-user-normal-game-find-all-results-theres-no-game-record"));

        /*
         * 사용자 - 노말만 잘 나오는지
         * -> 200
         * */
        LinkedMultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("gameId", "12345");
        params4.add("count", "20");
        params4.add("mode", Mode.NORMAL.getCode());
        mockMvc.perform(get("/pingpong/games/users/{intraId}", users[0].getIntraId()).contentType(MediaType.APPLICATION_JSON)
                .params(params4)
                .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken()))
//                .andExpect(jsonPath().value())
                .andExpect(status().isOk())
                .andDo(document("v1-user-normal-game-find-normal-results"));
    }



}
