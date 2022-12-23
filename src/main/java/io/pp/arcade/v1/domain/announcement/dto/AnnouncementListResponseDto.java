package io.pp.arcade.v1.domain.announcement.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AnnouncementListResponseDto {
    List<AnnouncementDto> announcements;
}
