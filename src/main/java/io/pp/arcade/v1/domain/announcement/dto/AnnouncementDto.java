package io.pp.arcade.v1.domain.announcement.dto;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementDto {
    private String content;

    public static AnnouncementDto from(AnnouncementAdmin announcement) {
        return AnnouncementDto.builder()
                .content(announcement.getContent())
                .build();
    }
}
