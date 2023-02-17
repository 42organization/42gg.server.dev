package io.pp.arcade.v1.admin.announcement.service;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminListResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminResponseDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
@Transactional
public class AnnouncementAdminService {
    private final AnnouncementAdminRepository announcementAdminRepository;

    @Transactional(readOnly = true)
    public AnnouncementAdminListResponseDto findAllAnnouncement(Pageable pageable) {
        Page<AnnouncementAdmin> allAnnouncements = announcementAdminRepository.findAll(pageable);
        Page<AnnouncementAdminResponseDto> responseDtos = allAnnouncements.map(AnnouncementAdminResponseDto::new);
        return new AnnouncementAdminListResponseDto(responseDtos.getContent(),
                responseDtos.getTotalPages(), responseDtos.getNumber() + 1);
    }
    @Transactional(readOnly = true)
    public AnnouncementAdminDto findAnnouncementExist() {//수정 필요 있음
        AnnouncementAdmin announcement = announcementAdminRepository.findFirstByOrderByIdDesc();
        if (announcement == null || announcement.getIsDel() == true) {
            return null;
        }
        AnnouncementAdminDto dto = AnnouncementAdminDto.from(announcement);
        return dto;
    }
    public void addAnnouncement(@RequestBody AnnouncementAdminAddDto addDto) {
        AnnouncementAdmin announcementAdmin = AnnouncementAdmin.builder()
                .content(addDto.getContent())
                .createdTime(addDto.getCreatedTime())
                .creatorIntraId(addDto.getCreatorIntraId())
                .build();
        announcementAdminRepository.save(announcementAdmin);
    }
    public AnnouncementAdmin modifyAnnouncementIsDel(@RequestBody AnnouncementAdminUpdateDto updateDto) {
        AnnouncementAdmin announcement = announcementAdminRepository.findFirstByOrderByIdDesc();
        announcement.update(updateDto.getDeleterIntraId(), updateDto.getDeletedTime());
        return announcement;
    }
}
