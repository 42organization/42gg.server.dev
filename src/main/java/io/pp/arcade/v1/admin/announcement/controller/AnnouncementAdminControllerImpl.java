package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.service.AnnouncementAdminService;
import io.pp.arcade.v1.global.exception.BusinessException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("pingpong/admin")
public class AnnouncementAdminControllerImpl implements AnnouncementAdminController{
    private final AnnouncementAdminService announcementAdminService;

    @Override
    @GetMapping("/announcements")
    public AnnouncementAdminListResponseDto announcementLists(HttpServletRequest request) {
        return AnnouncementAdminListResponseDto.builder()
                .announcements(announcementAdminService.findAllAnnouncement())
                .build();
    }

    @PostMapping("/announcements")//synchronized?
    public synchronized void announcementSave(AnnouncementAdminAddDto addDto, HttpServletRequest request) {

        AnnouncementAdminDto announcement = announcementAdminService.findAnnouncementExist();
        if (announcement != null ) {
            throw new BusinessException(("E0001"));
        }

        announcementAdminService.addAnnouncement(addDto);
    }

}
