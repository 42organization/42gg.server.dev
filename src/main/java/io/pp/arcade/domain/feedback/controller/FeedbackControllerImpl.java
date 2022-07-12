package io.pp.arcade.domain.feedback.controller;

import io.pp.arcade.domain.feedback.FeedbackRepository;
import io.pp.arcade.domain.feedback.FeedbackService;
import io.pp.arcade.domain.feedback.dto.FeedbackAddDto;
import io.pp.arcade.domain.feedback.dto.FeedbackRequestDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.FeedbackType;
import io.pp.arcade.global.util.HeaderUtil;
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
        if (saveReqDto.getContent().isBlank() == true) {
            throw new BusinessException("RP001");
        }
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        FeedbackAddDto feedbackAddDto = FeedbackAddDto.builder()
                .userId(user.getId())
                .category(saveReqDto.getCategory())
                .content(saveReqDto.getContent()).build();
        feedbackService.addFeedback(feedbackAddDto);
    }
}
