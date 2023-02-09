package io.pp.arcade.v1.admin.announcement.dto;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementAdminDto {
    private String content;
    private String creatorIntraId;
    private String deleterIntraId;
    private LocalDateTime createdTime;
    private LocalDateTime deletedTime;
    private Boolean isDel;

    static public AnnouncementAdminDto from(AnnouncementAdmin announcement) {
        return AnnouncementAdminDto.builder()
                .content(announcement.getContent())
                .creatorIntraId(announcement.getCreatorIntraId())
                .deleterIntraId(announcement.getDeleterIntraId())
                .createdTime(announcement.getCreatedTime())
                .deletedTime(announcement.getDeletedTime())
                .isDel(announcement.getIsDel())
                .build();
    }
}
