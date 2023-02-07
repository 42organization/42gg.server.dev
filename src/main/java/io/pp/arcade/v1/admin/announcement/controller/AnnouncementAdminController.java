package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementListResponseDto;
import javax.servlet.http.HttpServletRequest;

public interface AnnouncementAdminController {
    //get 요청
    public AnnouncementAdminListResponseDto announcementList(HttpServletRequest request);
    //post 요청
    public void announcementAdd(AnnouncementAdminAddDto addDto, HttpServletRequest request);
    //put 요청
    public void announcementModify(AnnouncementAdminUpdateDto updateDto, HttpServletRequest request);
}
