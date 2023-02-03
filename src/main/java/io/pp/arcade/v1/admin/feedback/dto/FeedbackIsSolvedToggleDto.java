package io.pp.arcade.v1.admin.feedback.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackIsSolvedToggleDto {
    private Integer feedbackId;
    private Boolean isSolved;
}
