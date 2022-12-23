package io.pp.arcade.v1.domain.announcement.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class AnnouncementAddRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String content;

    @Override
    public String toString() {
        return "AnnouncementAddRequestDto{" +
                "title=" + title +
                "content=" + content +
                '}';
    }
}
