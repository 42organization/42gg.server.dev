package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.service.AnnouncementAdminService;
import io.pp.arcade.v1.domain.announcement.AnnouncementService;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementListResponseDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.global.util.HeaderUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AnnouncementAdminControllerImpl implements AnnouncementAdminController{
    private final AnnouncementAdminService announcementAdminService;

    @Override
    @GetMapping("/announcements")
    public AnnouncementAdminListResponseDto announcementLists(HttpServletRequest request) {

        return AnnouncementAdminListResponseDto.builder()
                .announcements(announcementAdminService.findAllAnnouncement())
                .build();
    }
}
