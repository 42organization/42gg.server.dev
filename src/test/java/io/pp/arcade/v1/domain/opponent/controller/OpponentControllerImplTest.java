package io.pp.arcade.v1.domain.opponent.controller;

import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.v1.domain.opponent.Opponent;
import io.pp.arcade.v1.domain.opponent.OpponentRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
class OpponentControllerImplTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    OpponentRepository opponentRepository;


    Token token;
    @BeforeEach
    void init() {
        User user = userRepository.save(User.builder()
                .racketType(RacketType.NONE)
                .statusMessage("")
                .totalExp(0)
                .roleType(RoleType.USER)
                .ppp(1000)
                .imageUri("")
                .intraId("ha")
                .eMail("ha@pp.gg")
                .build());
        token = tokenRepository.save(new Token(user, "1", "1"));
        for (int i = 0; i < 12; i++) {
            opponentRepository.save(new Opponent("id" + i, "nick" + i, "", "hihi", i % 3 != 0));
        }
    }

    @Test
    @Transactional
    void findOpponentList() throws Exception {
        mockMvc.perform(get("/pingpong/match/opponent").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andDo(document("opponent-list"));
    }
}