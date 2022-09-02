package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.feedback.dto.FeedbackDto;
import io.pp.arcade.v1.domain.feedback.dto.FeedbackFindRequestDto;
import io.pp.arcade.v1.domain.admin.dto.update.FeedbackUpdateRequestDto;
import io.pp.arcade.v1.global.type.FeedbackType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FeedbackAdminController {
    void feedbackUpdate(@RequestBody FeedbackUpdateRequestDto updateRequestDto, HttpServletRequest request);
    void feedbackDelete(@PathVariable Integer feedbackId, HttpServletRequest request);
    List<FeedbackDto> feedbackAll(Pageable pageable, HttpServletRequest request);
    List<FeedbackDto> feedbackFindByCategory(@PathVariable FeedbackType category, Pageable pageable, HttpServletRequest request);
    List<FeedbackDto> feedbackFindByIsSolved(@PathVariable Boolean isSolved, Pageable pageable, HttpServletRequest request);
    List<FeedbackDto> feedbackFindByCategoryAndIsSolved(@RequestBody FeedbackFindRequestDto findRequestDto, Pageable pageable, HttpServletRequest request);
}
