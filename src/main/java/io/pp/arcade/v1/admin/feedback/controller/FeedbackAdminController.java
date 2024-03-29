package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleRequestDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackListAdminResponseDto;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


public interface FeedbackAdminController {
    FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(@RequestBody FeedbackIsSolvedToggleRequestDto updateDto);
    FeedbackListAdminResponseDto feedbackAll(@RequestParam(value = "page")int page, @RequestParam(defaultValue = "20")int size, HttpResponse httpResponse);
    FeedbackListAdminResponseDto feedbackFindByIntraId(@RequestParam(value = "q", required = false) String keyword,
                                                       @RequestParam(value = "page")int page,
                                                       @RequestParam(defaultValue = "20")int size,
                                                       HttpResponse httpResponse);
}
