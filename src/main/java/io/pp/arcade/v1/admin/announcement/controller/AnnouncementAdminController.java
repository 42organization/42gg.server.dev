package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import javax.servlet.http.HttpServletRequest;

public interface AnnouncementAdminController {
    public AnnouncementAdminListResponseDto announcementList(HttpServletRequest request);
    public void announcementAdd(AnnouncementAdminAddDto addDto, HttpServletRequest request);
    public void announcementModify(AnnouncementAdminUpdateDto updateDto, HttpServletRequest request);
}
