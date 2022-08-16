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

    @BeforeEach
    void init() {

    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 전체 (/games)")
    void gameResultAll() throws  Exception {

    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 랭크 (/games/rank)")
    void gameResultRankOnly() throws Exception {

    }

    @Test
    @Transactional
    @DisplayName("최근 게임 기록 - 노말 (/games/normal)")
    void gameResultNormalOnly() throws Exception {

    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 전체 (/games)")
    void gameResultByUserIdAndIndexAndCountAll() throws Exception {

    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 랭크 (/games/rank)")
    void gameResultByUserIdAndIndexAndCountRankOnly() throws Exception {

    }

    @Test
    @Transactional
    @DisplayName("개인 최근 게임 기록 - 노말 (/games/normal)")
    void gameResultByUserIdAndIndexAndCountNormalOnly() throws Exception {

    }

}