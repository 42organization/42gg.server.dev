package io.pp.arcade.v1.domain.announcement.controller;

import io.pp.arcade.v1.domain.announcement.AnnouncementService;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementAddRequestDto;
import io.pp.arcade.v1.domain.announcement.dto.AnnouncementListResponseDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("/announcements")
    public AnnouncementListResponseDto announcementLists(HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        return AnnouncementListResponseDto.builder()
                .announcements(announcementService.findAllAnnouncement())
                .build();
    }
}
