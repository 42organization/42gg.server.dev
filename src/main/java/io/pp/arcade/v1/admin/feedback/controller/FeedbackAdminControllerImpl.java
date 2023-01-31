package io.pp.arcade.v1.admin.feedback.controller;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminDto;
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
    public void feedbackIsSolvedUpdate(FeedbackIsSolvedUpdateRequestDto updateRequestDto, HttpServletRequest request){
        FeedbackIsSolvedUpdateDto updateDto = FeedbackIsSolvedUpdateDto.builder()
                .feedbackId(updateRequestDto.getFeedbackId())  //여기서 updateRequestDto의 정보를 긁어다가 올지 아니면 admindto에 한 번 감싸서 쓰는게 맞는지
                .isSolved(updateRequestDto.getIsSolved())
                .build();
        feedbackAdminService.updateFeedbackIsSolvedByAdmin(updateDto);
    }
}
