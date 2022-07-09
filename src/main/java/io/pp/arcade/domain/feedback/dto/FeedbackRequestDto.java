package io.pp.arcade.domain.feedback.dto;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
public class FeedbackRequestDto {
    @Length(max = 600)
    private String content;
}
