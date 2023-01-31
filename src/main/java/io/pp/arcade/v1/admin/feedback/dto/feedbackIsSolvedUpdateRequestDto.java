package io.pp.arcade.v1.admin.feedback.dto;

import lombok.Getter;

@Getter
public class feedbackIsSolvedUpdateRequestDto {
    private Integer feedbackId;
    private Boolean isSolved;
}
