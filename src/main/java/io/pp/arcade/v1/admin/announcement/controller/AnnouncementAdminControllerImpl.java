package io.pp.arcade.v1.admin.announcement.controller;

import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import io.pp.arcade.v1.admin.announcement.service.AnnouncementAdminService;
import io.pp.arcade.v1.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("pingpong/admin")
public class AnnouncementAdminControllerImpl implements AnnouncementAdminController{
    private final AnnouncementAdminService announcementAdminService;

    @Override
    @GetMapping("/announcement")
    public AnnouncementAdminListResponseDto announcementList(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "100") int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("createdTime").descending());
        return announcementAdminService.findAllAnnouncement(pageable);
    }

    @PostMapping("/announcement")
    public void announcementAdd(@RequestBody AnnouncementAdminAddDto addDto, HttpResponse httpResponse) {

        AnnouncementAdminDto announcement = announcementAdminService.findAnnouncementExist();
        if (announcement != null || isCreatedDataNull(addDto)) {
            httpResponse.setStatusCode((HttpStatus.SC_BAD_REQUEST));
            throw new BusinessException(("AC001"));
        }
        announcementAdminService.addAnnouncement(addDto);
    }

    @PutMapping("/announcement")
    public void announcementModify(@RequestBody AnnouncementAdminUpdateDto updateDto, HttpResponse httpResponse) {
        AnnouncementAdminDto announcement = announcementAdminService.findAnnouncementExist();
        if (announcement == null || isDeletedDataNull(updateDto)) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            throw new BusinessException("AD001");
        }
        announcementAdminService.modifyAnnouncementIsDel(updateDto);
    }
    private boolean isCreatedDataNull (AnnouncementAdminAddDto addDto) {
        if (addDto.getCreatorIntraId() == null) {
            return true;
        }
        if (addDto.getCreatedTime() == null) {
            return true;
        }
        if (addDto.getContent() == null) {
            return true;
        }
        return false;
    }
    private boolean isDeletedDataNull (AnnouncementAdminUpdateDto updateDto) {
        if (updateDto.getDeleterIntraId() == null) {
            return true;
        }
        if (updateDto.getDeletedTime() == null) {
            return true;
        }
        return false;
    }
}
