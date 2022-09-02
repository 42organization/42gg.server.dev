package io.pp.arcade.v1.domain.feedback.controller;

import io.pp.arcade.v1.domain.feedback.dto.FeedbackRequestDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface FeedbackController {
    void feedbackSave(@RequestBody @Validated FeedbackRequestDto saveReqDto,
                      HttpServletRequest request);
}
