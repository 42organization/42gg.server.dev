package io.pp.arcade.v1.admin.feedback.dto;

import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackAdminDto {
    private Integer id;
    //UserAdminDto 재휴키님께 받아서 생성하기
    private FeedbackType category;
    private String content;
}
