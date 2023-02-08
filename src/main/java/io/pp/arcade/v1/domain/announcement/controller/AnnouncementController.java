package io.pp.arcade.v1.domain.announcement.controller;

import io.pp.arcade.v1.domain.announcement.dto.AnnouncementResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface AnnouncementController {
    public AnnouncementResponseDto announcementLists(HttpServletRequest request);
}
