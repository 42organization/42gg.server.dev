package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleRequestDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface FeedbackAdminController {
    FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(@RequestBody FeedbackIsSolvedToggleRequestDto updateRequestDto, HttpServletRequest request);
}
