package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.*;
import io.pp.arcade.v1.admin.feedback.service.FeedbackAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/feedback")
public class FeedbackAdminControllerImpl implements FeedbackAdminController{

    private final FeedbackAdminService feedbackAdminService;
    @Override
    @PutMapping(value = "/is-solved")
    public FeedbackIsSolvedResponseDto feedbackIsSolvedToggle(FeedbackIsSolvedToggleRequestDto updateRequestDto){
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
    @GetMapping
    public FeedbackListAdminResponseDto feedbackAll(int page, int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1){
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return feedbackAdminService.findAllFeedbackByAdmin(pageable);
    }

    @Override
    @GetMapping(value = "/users")
    public FeedbackListAdminResponseDto feedbackFindByIntraId(String intraId, int page, int size, HttpResponse httpResponse) {
        /*
        if (page < 1 || size < 1){
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        */
        Pageable pageable = PageRequest.of(page - 1, size);
        return feedbackAdminService.findFeedbackByIntraId(intraId, pageable);
    }

}
