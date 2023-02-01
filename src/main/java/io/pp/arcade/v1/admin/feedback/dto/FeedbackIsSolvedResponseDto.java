package io.pp.arcade.v1.admin.feedback.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackIsSolvedResponseDto {
    private Boolean isSolved;

    @Override
    public String toString(){
        return "FeedbackIsSolvedResponseDto{" +
                "isSolved=" + isSolved +
                '}';
    }
}
