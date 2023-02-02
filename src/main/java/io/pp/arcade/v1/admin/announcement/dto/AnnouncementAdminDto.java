package io.pp.arcade.v1.admin.announcement.dto;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AnnouncementAdminDto {
    private String content;

    static public AnnouncementAdminDto from(AnnouncementAdmin announcement) {
        return AnnouncementAdminDto.builder()
                .content(announcement.getContent())
                .build();
    }

    @Builder
    public AnnouncementAdminDto(String content) {
        this.content = content;
    }
}
