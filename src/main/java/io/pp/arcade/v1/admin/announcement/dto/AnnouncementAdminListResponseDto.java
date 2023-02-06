package io.pp.arcade.v1.admin.announcement.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder// 페이징네이션 처리는 후순위
public class AnnouncementAdminListResponseDto {
    private List<AnnouncementAdminDto> announcements;
//    private int totalPage;
//    private int currentPage;
}
