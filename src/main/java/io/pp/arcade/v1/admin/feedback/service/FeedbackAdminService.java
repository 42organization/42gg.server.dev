package io.pp.arcade.v1.admin.feedback.service;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleDto;
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
    public void toggleFeedbackIsSolvedByAdmin(FeedbackIsSolvedToggleDto updateDto){
        Feedback feedback = feedbackAdminRepository.findById(updateDto.getFeedbackId()).orElseThrow(() -> new BusinessException("E0001"));
        if (feedback.getIsSolved() == true){
            feedback.setIsSolved(false);
        }else {
            feedback.setIsSolved(true);
        }
    }

    @Transactional
    public FeedbackAdminDto findFeedbackById(Integer feedbackId){
        Feedback feedback = feedbackAdminRepository.findById(feedbackId).orElseThrow(() -> new BusinessException("E0001"));
        FeedbackAdminDto feedbackAdminDto = FeedbackAdminDto.builder()
                .id(feedback.getId())
                .category(feedback.getCategory())
                .content(feedback.getContent())
                .isSolved(feedback.getIsSolved())
                .build();
        return feedbackAdminDto;
    }


}
