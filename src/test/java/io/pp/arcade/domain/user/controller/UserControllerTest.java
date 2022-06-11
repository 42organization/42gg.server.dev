package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.domain.user.dto.UserDto;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class UserControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PChangeService pChangeService;

    @Autowired
    private MockMvc mockMvc;

    UserDto user;

    @BeforeEach
    void init() {
        userService.addUser("donghyuk");
        user = userService.findByIntraId("donghyuk");
        for (int i = 0; i < 10; i++) {
            pChangeService.addPChange(PChangeAddDto.builder()
                    .gameId(i)
                    .userId(user.getId())
                    .pppChange(2)
                    .pppResult(2 + user.getPpp())
                    .build());
        }
    }

    @Test
    @Transactional
    void findUser() throws Exception {
        mockMvc.perform(get("/pingpong/users").contentType(MediaType.APPLICATION_JSON)
                .param("userId",user.getId().toString()))
                .andExpect(status().isOk())
                .andDo(document("find-user"));
    }

    @Test
    @Transactional
    void findDetailUser() throws Exception {
        mockMvc.perform(get("/pingpong/users/"+ user.getId().toString() +"/detail").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("find-user-detail"));
    }

    @Test
    @Transactional
    void findUserHistorics() throws Exception {
        mockMvc.perform(get("/pingpong/users/"+ user.getId().toString() +"/historics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("find-user-historics"));
    }
}