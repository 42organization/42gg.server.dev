package io.pp.arcade.v1.admin.feedback.service;

import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackAdminResponseDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackIsSolvedToggleDto;
import io.pp.arcade.v1.admin.feedback.dto.FeedbackListAdminResponseDto;
import io.pp.arcade.v1.admin.feedback.repository.FeedbackAdminRepository;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackAdminService {
    private final FeedbackAdminRepository feedbackAdminRepository;
    private final UserAdminRepository userAdminRepository;

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
    public FeedbackListAdminResponseDto findAllFeedbackByAdmin(Pageable pageable){
        Page<Feedback> feedbacks = feedbackAdminRepository.findAll(pageable);
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        FeedbackListAdminResponseDto responseDto = new FeedbackListAdminResponseDto(feedbackAdminResponseDtos.getContent(),
                feedbackAdminResponseDtos.getTotalPages(), feedbackAdminResponseDtos.getNumber() + 1);
        return responseDto;
    }

    @Transactional
    public FeedbackListAdminResponseDto findFeedbackByIntraId(String intraId, Pageable pageable){
        User user = userAdminRepository.findByIntraId(intraId).orElseThrow(() -> new BusinessException("E0001"));   //user가 없는 경우 E0001 발생
        Page<Feedback> feedbacks = (user == null)? null : feedbackAdminRepository.findAllByUser(user, pageable);     //해당 유저가 작성한 feedback이 없는 경우 null 반환
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        FeedbackListAdminResponseDto responseDto = new FeedbackListAdminResponseDto(feedbackAdminResponseDtos.getContent(),
                feedbackAdminResponseDtos.getTotalPages(), feedbackAdminResponseDtos.getNumber() + 1);
        return responseDto;
    }

}
