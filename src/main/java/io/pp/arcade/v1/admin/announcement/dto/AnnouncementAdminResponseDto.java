package io.pp.arcade.v1.admin.announcement.dto;

import io.pp.arcade.v1.domain.announcement.Announcement;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnnouncementAdminResponseDto {
    private String content;
    private String creatorIntraId;
    private String deleterIntraId;
    private LocalDateTime createdTime;
    private LocalDateTime deletedTime;
    private Boolean isDel;

    public AnnouncementAdminResponseDto(Announcement announcementAdmin)
    {
        this.content = announcementAdmin.getContent();
        this.creatorIntraId = announcementAdmin.getCreatorIntraId();
        this.deleterIntraId = announcementAdmin.getDeleterIntraId();
        this.createdTime = announcementAdmin.getCreatedTime();
        this.deletedTime = announcementAdmin.getDeletedTime();
        this.isDel = announcementAdmin.getIsDel();
    }
}
