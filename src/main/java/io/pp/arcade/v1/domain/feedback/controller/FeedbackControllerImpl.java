package io.pp.arcade.v1.domain.feedback.controller;

import io.pp.arcade.v1.domain.feedback.FeedbackService;
import io.pp.arcade.v1.domain.feedback.dto.FeedbackAddDto;
import io.pp.arcade.v1.domain.feedback.dto.FeedbackRequestDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class FeedbackControllerImpl implements FeedbackController {
    private final TokenService tokenService;
    private final FeedbackService feedbackService;

    @Override
    @PostMapping(value = "/feedback")
    public void feedbackSave(FeedbackRequestDto saveReqDto, HttpServletRequest request) {
        if (saveReqDto.getContent().isBlank()) {
            throw new BusinessException("RP001");
        }
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        FeedbackAddDto feedbackAddDto = FeedbackAddDto.builder()
                .user(user)
                .category(saveReqDto.getCategory())
                .content(saveReqDto.getContent()).build();
        feedbackService.addFeedback(feedbackAddDto);
    }
}
