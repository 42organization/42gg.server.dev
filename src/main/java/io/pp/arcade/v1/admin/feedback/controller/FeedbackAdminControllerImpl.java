package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedUpdateDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedUpdateRequestDto;
import io.pp.arcade.v1.admin.feedback.service.FeedbackAdminService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class FeedbackAdminControllerImpl implements FeedbackAdminController{

    private final FeedbackAdminService feedbackAdminService;
    @Override
    @PutMapping(value = "/feedback/is-solved")
    public FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(FeedbackIsSolvedUpdateRequestDto updateRequestDto, HttpServletRequest request){
        FeedbackAdminDto feedbackAdminDto = feedbackAdminService.findFeedbackById(updateRequestDto.getFeedbackId());

        FeedbackIsSolvedUpdateDto updateDto = FeedbackIsSolvedUpdateDto.builder()
                .feedbackId(feedbackAdminDto.getId())
                .isSolved(feedbackAdminDto.getIsSolved())
                .build();

        feedbackAdminService.toggleFeedbackIsSolvedByAdmin(updateDto);
        FeedbackIsSolvedResponseDto responseDto = FeedbackIsSolvedResponseDto.builder()
                .isSolved(updateDto.getIsSolved())
                .build();
        return responseDto;
    }
}
