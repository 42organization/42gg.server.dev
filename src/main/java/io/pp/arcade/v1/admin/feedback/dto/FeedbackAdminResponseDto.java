package io.pp.arcade.v1.admin.feedback.dto;

import io.pp.arcade.v1.admin.users.dto.UserAdminDto;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackAdminResponseDto {
    private Integer id;
    private UserAdminDto user;
    private FeedbackType category;
    private String content;
    private Boolean isSolved;

    public static FeedbackAdminResponseDto from(Feedback feedback){
        return FeedbackAdminResponseDto.builder()
                .id(feedback.getId())
                .user(UserAdminDto.from(feedback.getUser()))
                .category(feedback.getCategory())
                .content(feedback.getContent())
                .isSolved(feedback.getIsSolved())
                .build();
    }

    @Override
    public String toString(){
        return "FeedbackAdminResponseDto{" +
                "id=" + id +
                ", user=" + user +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", isSolved=" + isSolved +
                '}';
    }
}
