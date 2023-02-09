package io.pp.arcade.v1.admin.announcement.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnnouncementAdminListResponseDto {
    private List<AnnouncementAdminResponseDto> announcementList;
    private int totalPage;
    private int currentPage;
}
