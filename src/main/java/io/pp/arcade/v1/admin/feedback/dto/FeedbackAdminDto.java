package io.pp.arcade.v1.admin.feedback.dto;

import io.pp.arcade.v1.admin.users.dto.UserAdminDto;
import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
public class FeedbackAdminDto {
    private Integer id;
    private String intraId;
    private Date createdTime;
    private FeedbackType category;
    private String content;
    private Boolean isSolved;

    public static FeedbackAdminDto from(Feedback feedback){
        return FeedbackAdminDto.builder()
                .id(feedback.getId())
                .intraId(feedback.getUser().getIntraId())
                .createdTime(Date.valueOf(feedback.getCreatedAt().toLocalDate()))
                .category(feedback.getCategory())
                .content(feedback.getContent())
                .isSolved(feedback.getIsSolved())
                .build();
    }

    @Override
    public String toString(){
        return "FeedbackAdminDto{" +
                "id=" + id +
                ", intraId=" + intraId +
                ", createdTime=" + createdTime +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", isSolved=" + isSolved +
                '}';
    }

}
