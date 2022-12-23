package io.pp.arcade.v1.domain.announcement.dto;

import io.pp.arcade.v1.domain.announcement.Announcement;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnnouncementDto {
    private String title;
    private List<String> content;

    private String link;

    static public AnnouncementDto from(Announcement announcement) {
        return AnnouncementDto.builder()
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .link(announcement.getLink())
                .build();
    }

    @Builder
    public AnnouncementDto(String title, String content, String link) {
        List<String> contents = List.of(content.split("\n"));
        this.title = title;
        this.content = contents;
        this.link = link;
    }
}
