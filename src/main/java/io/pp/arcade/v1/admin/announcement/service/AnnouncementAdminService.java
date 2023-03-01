package io.pp.arcade.v1.admin.announcement.service;

import io.pp.arcade.v1.domain.announcement.Announcement;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AnnouncementAdminService {
    private final AnnouncementAdminRepository announcementAdminRepository;

    @Transactional(readOnly = true)
    public AnnouncementAdminListResponseDto findAllAnnouncement(Pageable pageable) {
        Page<Announcement> allAnnouncements = announcementAdminRepository.findAll(pageable);
        Page<AnnouncementAdminResponseDto> responseDtos = allAnnouncements.map(AnnouncementAdminResponseDto::new);
        return new AnnouncementAdminListResponseDto(responseDtos.getContent(),
                responseDtos.getTotalPages(), responseDtos.getNumber() + 1);
    }
    @Transactional(readOnly = true)
    public AnnouncementAdminDto findAnnouncementExist() {//수정 필요 있음
        Announcement announcement = announcementAdminRepository.findFirstByOrderByIdDesc();
        if (announcement == null || announcement.getIsDel() == true) {
            return null;
        }
        AnnouncementAdminDto dto = AnnouncementAdminDto.from(announcement);
        return dto;
    }
    public void addAnnouncement(AnnouncementAdminAddDto addDto) {
        Announcement announcementAdmin = Announcement.builder()
                .content(addDto.getContent())
                .createdTime(addDto.getCreatedTime())
                .creatorIntraId(addDto.getCreatorIntraId())
                .build();
        announcementAdminRepository.save(announcementAdmin);
    }
    public Announcement modifyAnnouncementIsDel(AnnouncementAdminUpdateDto updateDto) {
        Announcement announcement = announcementAdminRepository.findFirstByOrderByIdDesc();
        announcement.update(updateDto.getDeleterIntraId(), updateDto.getDeletedTime());
        return announcement;
    }
}
