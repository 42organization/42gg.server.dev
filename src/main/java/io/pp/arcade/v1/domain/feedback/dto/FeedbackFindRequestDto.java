package io.pp.arcade.v1.domain.feedback.dto;

import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Getter;

@Getter
public class FeedbackFindRequestDto {
    private FeedbackType category;
    private Boolean isSolved;
}
