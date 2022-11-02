package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
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

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class RankControllerNormalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestInitiator testInitiator;

    private User[] users;
    private Token[] tokens;
    private Team[] teams;
    private Slot[] slots;

    @BeforeEach
    void init(){
        testInitiator.letsgo();
        users = testInitiator.users;
        tokens = testInitiator.tokens;
        teams = testInitiator.teams;
        slots = testInitiator.slots;
    }

    @Test
    @Transactional
    @DisplayName("노말용 VIP 페이지 (/vip)")
    void vipList() throws Exception {
        String page;
        String count;
        ResultActions actions;
        /* 유저가 존재히지 않을 경우  -> 얘기를 해봐야 함니다 */
//        mockMvc.perform((get("/pingpong/vip").contentType(MediaType.APPLICATION_JSON)
//                        .param("page","1"))
//                        .header("Authorization", "Bearer " + 0)) // header 해줘야함
//                .andExpect(jsonPath("$.myRank").value()) //
//                .andExpect(jsonPath("$.currentPage").value(1)) //
//                .andExpect(jsonPath("$.totalPage").value(1)) //
//                .andExpect(jsonPath("$.rankList").isEmpty()) //
//                .andExpect(status().isOk())
//                .andDo(document("vip-List"));

        /* 유저가 존재할 경우 */
        mockMvc.perform((get("/pingpong/vip").contentType(MediaType.APPLICATION_JSON))
                        .param("page","1")
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken())) // header 해줘야함
                .andExpect(jsonPath("$.myRank").value(1)) //
                .andExpect(jsonPath("$.rankList[0].intraId").value("hhakim")) //
                .andExpect(jsonPath("$.rankList[0].statusMessage").value("kikikaka")) //
                .andExpect(jsonPath("$.rankList[0].level").value(7)) //
                .andExpect(jsonPath("$.rankList[0].exp").value(users[0].getTotalExp())) //
                .andExpect(jsonPath("$.rankList[11].intraId").value("hjujeon")) //
                .andExpect(jsonPath("$.rankList[11].statusMessage").value("kiki")) //
                .andExpect(jsonPath("$.rankList[11].level").value(1)) //
                .andExpect(jsonPath("$.rankList[11].exp").value(users[11].getTotalExp())) //
                .andExpect(status().isOk())
                .andDo(document("vip-List"));

        /* 유저가 존재할 경우 */
        mockMvc.perform((get("/pingpong/vip").contentType(MediaType.APPLICATION_JSON))
                        .param("page","1")
                        .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken())) // header 해줘야함
                .andExpect(jsonPath("$.myRank").value(1)) //
                .andExpect(jsonPath("$.rankList[0].intraId").value("hhakim")) //
                .andExpect(jsonPath("$.rankList[0].statusMessage").value("kikikaka")) //
                .andExpect(jsonPath("$.rankList[0].level").value(7)) //
                .andExpect(jsonPath("$.rankList[0].exp").value(users[0].getTotalExp())) //
                .andExpect(jsonPath("$.rankList[11].intraId").value("hjujeon")) //
                .andExpect(jsonPath("$.rankList[11].statusMessage").value("kiki")) //
                .andExpect(jsonPath("$.rankList[11].level").value(1)) //
                .andExpect(jsonPath("$.rankList[11].exp").value(users[11].getTotalExp())) //
                .andExpect(status().isOk())
                .andDo(document("vip-List"));

        /*
         * 랭킹 페이지 - 일반전 다음 페이지 조회
         * page = 2
         * count = 10
         * season = null
         * */
        page = "2";
        count = "10";
        actions = mockMvc.perform((get("/pingpong/vip").contentType(MediaType.APPLICATION_JSON))
                .param("page","2")
                .param("count", count)
                .header("Authorization", "Bearer " + testInitiator.tokens[0].getAccessToken()));
        actions.andExpect(jsonPath("$.rankList[0].rank").value(Integer.parseInt(count) * (Integer.parseInt(page) - 1) + 1));
        actions.andExpect(status().isOk()).andDo(document("v1-ranking-find-all-list-count-is-10-and-next-page"));
    }
}