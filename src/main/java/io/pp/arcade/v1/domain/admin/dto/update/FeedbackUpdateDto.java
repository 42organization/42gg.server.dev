package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackUpdateDto {
    private Integer feedbackId;
    private Boolean isSolved;
}
