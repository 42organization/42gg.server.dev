package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.*;
import io.pp.arcade.v1.admin.feedback.service.FeedbackAdminService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class FeedbackAdminControllerImpl implements FeedbackAdminController{

    private final FeedbackAdminService feedbackAdminService;
    @Override
    @PutMapping(value = "/feedback/is-solved")
    public FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(FeedbackIsSolvedToggleRequestDto updateRequestDto, HttpServletRequest request){
        FeedbackAdminDto feedbackAdminDto = feedbackAdminService.findFeedbackById(updateRequestDto.getFeedbackId());

        FeedbackIsSolvedToggleDto updateDto = FeedbackIsSolvedToggleDto.builder()
                .feedbackId(feedbackAdminDto.getId())
                .isSolved(feedbackAdminDto.getIsSolved())
                .build();

        feedbackAdminService.toggleFeedbackIsSolvedByAdmin(updateDto);
        FeedbackIsSolvedResponseDto responseDto = FeedbackIsSolvedResponseDto.builder()
                .isSolved(updateDto.getIsSolved())
                .build();
        return responseDto;
    }

    @Override
    @GetMapping(value = "/feedback")
    public List<FeedbackAdminResponseDto> feedbackAll(Pageable pageable, HttpServletRequest request) {
        return feedbackAdminService.findFeedbackByAdmin(pageable);
    }


}
