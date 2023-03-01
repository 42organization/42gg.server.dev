package io.pp.arcade.v1.domain.announcement.dto;

import io.pp.arcade.v1.domain.announcement.Announcement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementDto {
    private String content;

    public static AnnouncementDto from(Announcement announcement) {
        return AnnouncementDto.builder()
                .content(announcement.getContent())
                .build();
    }
}
