package io.pp.arcade.domain.game.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PChangeRepository pChangeRepository;


    @BeforeEach
    void init() {

    }


    @Test
    @Transactional
    void gameUserInfo() {
    }

    @Test
    @Transactional
    void saveGameResult() {
    }

    @Test
    @Transactional
    void gameResultByCount() {
    }

    @Test
    @Transactional
    void gameResultByIndexAndCount() {
    }

    @Test
    @Transactional
    void gameResultByUserIdAndIndexAndCount() {
    }
}