package io.pp.arcade.v1.admin.announcement.service;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional//나중에 고칠 필요 있음
public class AnnouncementAdminService {
    private final AnnouncementAdminRepository announcementAdminRepository;

    @Transactional(readOnly = true)
    public List<AnnouncementAdminDto> findAllAnnouncement() {
        List<AnnouncementAdmin> announcements = announcementAdminRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<AnnouncementAdminDto> adminDtoList = null;
        if (announcements != null) {
            adminDtoList = announcements.stream().map(AnnouncementAdminDto::from).collect(Collectors.toList());
        }
        return adminDtoList;
    }
    @Transactional(readOnly = true)
    public AnnouncementAdminDto findAnnouncementExist() {//수정 필요 있음
        List<AnnouncementAdmin> announcements = announcementAdminRepository.findAllByIsDelFalse().orElse(null);
        if (announcements.size() == 0) {
            return null;
        }
        AnnouncementAdminDto dto = AnnouncementAdminDto.from(announcements.get(0));
        return dto;
    }

    public void addAnnouncement(AnnouncementAdminAddDto addDto) {
        AnnouncementAdmin announcementAdmin = AnnouncementAdmin.builder()
                .content(addDto.getContent())
                .createdTime(addDto.getCreatedTime())
                .creatorIntraId(addDto.getCreatorIntraId())
                .deleterIntraId(null)
                .deletedTime(null)
                .build();
        announcementAdminRepository.save(announcementAdmin);
    }
}
