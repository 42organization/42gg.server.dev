package io.pp.arcade.v1.admin.announcement.service;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminAddDto;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminUpdateDto;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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

    public void addAnnouncement(@RequestBody AnnouncementAdminAddDto addDto) {
        AnnouncementAdmin announcementAdmin = AnnouncementAdmin.builder()
                .content(addDto.getContent())
                .createdTime(addDto.getCreatedTime())
                .creatorIntraId(addDto.getCreatorIntraId())
                .build();
        announcementAdminRepository.save(announcementAdmin);
    }

    public AnnouncementAdmin modifyAnnouncementIsDel(@RequestBody AnnouncementAdminUpdateDto updateDto) {
        AnnouncementAdmin announcement = announcementAdminRepository.findByIsDelFalse().orElse(null);
        announcement.setIsDel(true);
        announcement.setDeleterIntraId(updateDto.getDeleterIntraId());
        announcement.setDeletedTime(updateDto.getDeletedTime());

        return announcement;
    }
}
