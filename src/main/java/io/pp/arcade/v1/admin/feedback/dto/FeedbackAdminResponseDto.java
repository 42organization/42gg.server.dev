package io.pp.arcade.v1.admin.feedback.dto;

import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.global.type.FeedbackType;
import lombok.Getter;
import java.sql.Date;

@Getter
public class FeedbackAdminResponseDto {
    private Integer id;
    private String intraId;
    private Date createdTime;
    private FeedbackType category;
    private String content;
    private Boolean isSolved;

    public FeedbackAdminResponseDto(Feedback feedback){
        this.id = feedback.getId();
        this.intraId = feedback.getUser().getIntraId();
        this.createdTime = Date.valueOf(feedback.getCreatedAt().toLocalDate());
        this.category = feedback.getCategory();
        this.content = feedback.getContent();
        this.isSolved = feedback.getIsSolved();
    }

    @Override
    public String toString(){
        return "FeedbackAdminResponseDto{" +
                "id=" + id +
                ", intraId=" + intraId +
                ", createdTime=" + createdTime +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", isSolved=" + isSolved +
                '}';
    }
}
