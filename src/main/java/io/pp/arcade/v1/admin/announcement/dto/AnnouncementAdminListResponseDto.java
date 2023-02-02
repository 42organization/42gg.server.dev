package io.pp.arcade.v1.admin.announcement.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementAdminListResponseDto {
    List<AnnouncementAdminDto> announcements;
}
