package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class FeedbackUpdateRequestDto {
    private Integer feedbackId;
    private Boolean isSolved;
}
