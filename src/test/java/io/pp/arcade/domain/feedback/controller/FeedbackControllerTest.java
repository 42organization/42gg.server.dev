package io.pp.arcade.domain.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.RestDocsConfiguration;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.feedback.Feedback;
import io.pp.arcade.domain.feedback.FeedbackRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.FeedbackType;
import org.assertj.core.api.Assertions;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
public class FeedbackControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TestInitiator initiator;
    @Autowired
    private FeedbackRepository feedbackRepository;

    User user1;
    Feedback alreadySavedFeedback;

    @BeforeEach
    void init() {
        initiator.letsgo();
        user1 = initiator.users[1];

        alreadySavedFeedback = Feedback.builder()
                .user(user1)
                .category(FeedbackType.ETC)
                .content("프론트 너무 잘 만들었어요 예뻐요")
                .build();

        feedbackRepository.save(alreadySavedFeedback);
    }

    @Test
    @Transactional
    @DisplayName("건의 사항 요청")
    void FeedbackSave() throws Exception {
        /*
        category가 숫자인 경우
         */

        /*
        category가 다른 string일 경우
         */

        Map<String, String> body = new HashMap<>();
        body.put("content", "방금 한 게임 스코어 0:2에서 2:0으로 바꿔주세요.");
        mockMvc.perform(post("/pingpong/feedback/{category}", FeedbackType.GAMERESULT.getCode()).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + initiator.tokens[0].getAccessToken())
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(document("feedback-save"));

        int size = feedbackRepository.findAll().size();
        Assertions.assertThat(size).isEqualTo(2);

        List<Feedback> feedbacks = feedbackRepository.findAll();
        for(Feedback f: feedbacks) {
            System.out.println(f.getCategory() + ": ");
            System.out.println(f.getContent());
            System.out.println(f.getIsSolved());
        }
    }
}
