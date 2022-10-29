package io.pp.arcade.v1.domain.feedback.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackAddDto {
    private UserDto user;
    private FeedbackType category;
    private String content;

    @Override
    public String toString() {
        return "FeedbackAddDto{" +
                "user=" + user.toString() +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
