package io.pp.arcade.v1.admin.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FeedbackListAdminResponseDto {
    private List<FeedbackAdminResponseDto> feedbackList;
    private int totalPage;
    private int currentPage;
}
