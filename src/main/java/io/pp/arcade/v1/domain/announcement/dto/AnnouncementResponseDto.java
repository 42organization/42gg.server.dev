package io.pp.arcade.v1.domain.announcement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementResponseDto {
    private String content;
}
