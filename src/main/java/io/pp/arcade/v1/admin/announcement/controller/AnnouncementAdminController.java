package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementListResponseDto;
import javax.servlet.http.HttpServletRequest;

public interface AnnouncementAdminController {
    //get 요청
    public AnnouncementAdminListResponseDto announcementLists(HttpServletRequest request);
}
