package io.pp.arcade.v1.domain.announcement.controller;

import io.pp.arcade.v1.domain.announcement.AnnouncementService;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementResponseDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/pingpong")
public class AnnouncementControllerImpl implements AnnouncementController {
    private final AnnouncementService announcementService;
    private final TokenService tokenService;

    @Override
    @GetMapping("/announcement")
    public AnnouncementResponseDto announcementLists(HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        return AnnouncementResponseDto.builder()
                .announcement(announcementService.findAllAnnouncement().get(0))
                .build();
    }
}
