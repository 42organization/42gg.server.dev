package io.gg.arcade.domain.slot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gg.arcade.RestDocsConfiguration;
import io.gg.arcade.domain.slot.dto.SlotDto;
import io.gg.arcade.domain.slot.dto.SlotModifyRequestDto;
import io.gg.arcade.domain.slot.repository.SlotRepository;
import io.gg.arcade.domain.slot.service.SlotService;
import io.gg.arcade.domain.team.service.TeamService;
import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.service.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SlotControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SlotService slotService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    UserDto user1;
    UserDto user2;
    UserDto user3;
    UserDto user4;
    SlotDto slot1;
    SlotDto slot2;

    @BeforeAll
    void init() {
        slotService.addTodaySlots();
        userService.addUser(UserAddRequestDto.builder()
                .intraId("nheo")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("donghyuk")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("hakim")
                .userImgUri("")
                .build());
        userService.addUser(UserAddRequestDto.builder()
                .intraId("jiyun")
                .userImgUri("")
                .build());
        user1 = userService.findByIntraId("nheo");
        user2 = userService.findByIntraId("donghyuk");
        user3 = userService.findByIntraId("hakim");
        user4 = userService.findByIntraId("jiyun");
        slot1 = slotService.findSlotById(slotRepository.findAll().get(0).getId());
        slot2 = slotService.findSlotById(slotRepository.findAll().get(1).getId());
    }

    @Test
    @Transactional
    void findSlots() throws Exception{
        SlotModifyRequestDto modifyRequestDto = SlotModifyRequestDto.builder()
                .slotId(slot1.getId())
                .gamePpp(user2.getPpp())
                .type("double")
                .build();
        slotService.addUserInSlot(modifyRequestDto);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "single");
        params.add("userId", String.valueOf(user1.getId()));


        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-slots"));
    }

    @Test
    @Transactional
    void addUserInSlot() throws Exception{
        SlotModifyRequestDto modifyRequestDto = SlotModifyRequestDto.builder()
                .slotId(slot1.getId())
                .gamePpp(user2.getPpp())
                .type("double")
                .build();
        slotService.addUserInSlot(modifyRequestDto);

        Map<String, String> body = new HashMap<>();
        body.put("slotId", String.valueOf(slot2.getId()));
        body.put("gamePpp", String.valueOf(slot2.getGamePpp()));
        body.put("type", "single");

        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(user2.getId()))
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user3.getId()))
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("add-user-in-slot"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("type", "single");
        params.add("userId", String.valueOf(user4.getId()));

        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-slots-after-adduser"));
    }

    @Test
    void removeUserInSlot() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("slotId", String.valueOf(slot2.getId()));
        body.put("gamePpp", String.valueOf(slot2.getGamePpp()));
        body.put("type", "single");

        mockMvc.perform(post("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(user3.getId()))
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "single");
        params.add("userId", String.valueOf(user4.getId()));

        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-slots-before-removeuser"));

        mockMvc.perform(delete("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                .param("matchId", String.valueOf(slot2.getId())))
                .andExpect(status().isOk())
                .andDo(document("remove-user-in-slot"));

        mockMvc.perform(get("/pingpong/match/tables/1").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(document("find-slots-after-removeuser"));
    }
}