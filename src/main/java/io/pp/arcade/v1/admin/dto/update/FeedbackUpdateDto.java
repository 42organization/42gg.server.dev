package io.pp.arcade.v1.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackUpdateDto {
    private Integer feedbackId;
    private Boolean isSolved;
}
