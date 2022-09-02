package io.pp.arcade.v1.domain.feedback.dto;

import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
public class FeedbackRequestDto {
    private FeedbackType category;
    @Length(max = 600)
    private String content;
}
