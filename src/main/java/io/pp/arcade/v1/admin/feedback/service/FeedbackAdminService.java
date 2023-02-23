package io.pp.arcade.v1.admin.feedback.service;

import io.pp.arcade.v1.admin.feedback.dto.*;
import io.pp.arcade.v1.admin.feedback.repository.FeedbackAdminRepository;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackAdminService {
    private final FeedbackAdminRepository feedbackAdminRepository;
    private final UserAdminRepository userAdminRepository;

    @Transactional
    public FeedbackIsSolvedResponseDto toggleFeedbackIsSolvedByAdmin(FeedbackIsSolvedToggleDto updateDto){
        Feedback feedback = feedbackAdminRepository.findById(updateDto.getFeedbackId()).orElseThrow(() -> new BusinessException("E0001"));
        if (feedback.getIsSolved() == true){
            feedback.setIsSolved(false);
        }else {
            feedback.setIsSolved(true);
        }
        FeedbackIsSolvedResponseDto responseDto = FeedbackIsSolvedResponseDto.builder()
                .isSolved(feedback.getIsSolved())
                .build();
        return responseDto;
    }

    @Transactional(readOnly = true)
    public FeedbackAdminDto findFeedbackById(Integer feedbackId){
        Feedback feedback = feedbackAdminRepository.findById(feedbackId).orElseThrow(() -> new BusinessException("E0001"));
        FeedbackAdminDto feedbackAdminDto = FeedbackAdminDto.builder()
                .id(feedback.getId())
                .intraId(feedback.getUser().getIntraId())
                .createdTime(Date.valueOf(feedback.getCreatedAt().toLocalDate()))
                .category(feedback.getCategory())
                .content(feedback.getContent())
                .isSolved(feedback.getIsSolved())
                .build();
        return feedbackAdminDto;
    }

    @Transactional(readOnly = true)
    public FeedbackListAdminResponseDto findAllFeedbackByAdmin(Pageable pageable){
        Page<Feedback> feedbacks = feedbackAdminRepository.findAll(pageable);
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        FeedbackListAdminResponseDto responseDto = new FeedbackListAdminResponseDto(feedbackAdminResponseDtos.getContent(),
                feedbackAdminResponseDtos.getTotalPages(), feedbackAdminResponseDtos.getNumber() + 1);
        return responseDto;
    }
/*
    @Transactional(readOnly = true)
    public FeedbackListAdminResponseDto findFeedbackByIntraId(String intraId, Pageable pageable){
        User user = userAdminRepository.findByIntraId(intraId).orElseThrow(() -> new BusinessException("E0001"));   //user가 없는 경우 E0001 발생
        Page<Feedback> feedbacks = (user == null)? null : feedbackAdminRepository.findAllByUser(user, pageable);     //해당 유저가 작성한 feedback이 없는 경우 null 반환
        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbacks.map(FeedbackAdminResponseDto::new);
        FeedbackListAdminResponseDto responseDto = new FeedbackListAdminResponseDto(feedbackAdminResponseDtos.getContent(),
                feedbackAdminResponseDtos.getTotalPages(), feedbackAdminResponseDtos.getNumber() + 1);
        return responseDto;
    }

 */

    @Transactional
    public FeedbackListAdminResponseDto findByPartsOfIntraId(String keyword, Pageable pageable) {
        List<User> userList= userAdminRepository.findByIntraIdContains(keyword); //해당 keyword를 가진 유저 모두 찾아오기
        int usersize = (int) userList.size();  //위에서 찾은 유저의 개수

        List<Feedback> feedbackList = feedbackAdminRepository.findAllByUser(userList.get(0));
        for(int i=1;i<usersize;i++){
            List<Feedback> feedbacks = feedbackAdminRepository.findAllByUser(userList.get(i));   //해당 유저의 모든 피드백
            for(int j=0;j<feedbacks.size();j++){
                feedbackList.add(feedbacks.get(j));
            }
        }

        /*feedback list -> page*/
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), (int) feedbackList.size());
        Page<Feedback> feedbackPage = new PageImpl<>(feedbackList.subList(start, end), pageable, feedbackList.size());

        Page<FeedbackAdminResponseDto> feedbackAdminResponseDtos = feedbackPage.map(FeedbackAdminResponseDto::new);
        FeedbackListAdminResponseDto responseDto = new FeedbackListAdminResponseDto(feedbackAdminResponseDtos.getContent(),
                feedbackAdminResponseDtos.getTotalPages(), feedbackAdminResponseDtos.getNumber() + 1);

        return responseDto;
    }

}
