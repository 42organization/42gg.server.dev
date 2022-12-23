package io.pp.arcade.v1.domain.announcement.controller;

import io.pp.arcade.v1.domain.announcement.dto.AnnouncementAddRequestDto;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementDto;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementListResponseDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

public interface AnnouncementController {
    public AnnouncementListResponseDto announcementLists(HttpServletRequest request);
    public void announcementAdd(@RequestBody @Valid AnnouncementAddRequestDto addReqDto, HttpServletRequest request);
}
