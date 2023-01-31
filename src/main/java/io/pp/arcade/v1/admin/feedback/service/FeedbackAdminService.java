package io.pp.arcade.v1.admin.feedback.service;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedUpdateDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedUpdateRequestDto;
import io.pp.arcade.v1.admin.feedback.repository.FeedbackAdminRepository;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class FeedbackAdminService {
    private FeedbackAdminRepository feedbackAdminRepository;

    @Transactional
    public void updateFeedbackIsSolvedByAdmin(FeedbackIsSolvedUpdateDto updateDto){
        Feedback feedback = feedbackAdminRepository.findById(updateDto.getFeedbackId()).orElseThrow(() -> new BusinessException("E0001"));
        feedback.setIsSolved(updateDto.getIsSolved());
    }
    //feedback 찾는 method 추가하기


}
