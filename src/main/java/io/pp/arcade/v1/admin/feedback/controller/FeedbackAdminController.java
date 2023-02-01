package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FeedbackAdminController {
    FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(@RequestBody FeedbackIsSolvedToggleRequestDto updateRequestDto, HttpServletRequest request);
    List<FeedbackAdminResponseDto> feedbackAll(Pageable pageable, HttpServletRequest request);
    List<FeedbackAdminResponseDto> feedbackFindByIntraId(@RequestParam(value = "q")String intraId, Pageable pageable ,HttpServletRequest request);
}
