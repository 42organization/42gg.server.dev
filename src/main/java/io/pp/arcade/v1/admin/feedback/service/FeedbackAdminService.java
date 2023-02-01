package io.pp.arcade.v1.admin.feedback.service;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleDto;
import io.pp.arcade.v1.admin.feedback.repository.FeedbackAdminRepository;
import io.pp.arcade.v1.admin.users.dto.UserAdminDto;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackAdminService {
    private FeedbackAdminRepository feedbackAdminRepository;
    private UserAdminRepository userAdminRepository;

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

    @Transactional
    public List<FeedbackAdminResponseDto> findAllFeedbackByAdmin(Pageable pageable){
        Page<Feedback> feedbacks = feedbackAdminRepository.findAll(pageable);
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        return feedbackAdminResponseDtos.getContent();
    }

    @Transactional
    public List<FeedbackAdminResponseDto> findFeedbackByIntraId(String intraId, Pageable pageable){
        User user = userAdminRepository.findByIntraId(intraId).orElseThrow(() -> new BusinessException("E0001"));
        Page<Feedback> feedbacks = feedbackAdminRepository.findAllByUser(user);
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        return feedbackAdminResponseDtos.getContent();
    }

}
