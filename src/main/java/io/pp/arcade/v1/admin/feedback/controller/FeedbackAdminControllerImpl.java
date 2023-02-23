package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.*;
import io.pp.arcade.v1.admin.feedback.service.FeedbackAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;


@RestController
@RequiredArgsConstructor
@RequestMapping("pingpong/admin/feedback")
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
        return feedbackAdminService.toggleFeedbackIsSolvedByAdmin(updateDto);
    }

    @Override
    @GetMapping
    public FeedbackListAdminResponseDto feedbackAll(int page, int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1){
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("isSolved").and(Sort.by("createdAt")));
        return feedbackAdminService.findAllFeedbackByAdmin(pageable);
    }

    @Override
    @GetMapping(value = "/users")
    public FeedbackListAdminResponseDto feedbackFindByIntraId(String keyword, int page, int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1){
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("intra_id").and(Sort.by("createdAt")));
        if (keyword == null){    //keyword가 없는 경우 모든 피드백 반환
            return feedbackAdminService.findAllFeedbackByAdmin(pageable);
        }
        else {
            return feedbackAdminService.findByPartsOfIntraId(keyword, pageable);
        }
    }
}
