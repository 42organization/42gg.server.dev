package io.pp.arcade.v1.domain.feedback.dto;

import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackAddDto {
    private Integer userId;
    private FeedbackType category;
    private String content;

    @Override
    public String toString() {
        return "FeedbackAddDto{" +
                "userId=" + userId +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
