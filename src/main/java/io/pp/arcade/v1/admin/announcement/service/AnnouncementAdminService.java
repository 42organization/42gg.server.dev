package io.pp.arcade.v1.admin.announcement.service;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.admin.announcement.dto.AnnouncementAdminDto;
import io.pp.arcade.v1.admin.announcement.repository.AnnouncementAdminRepository;
import io.pp.arcade.v1.domain.announcement.Announcement;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AnnouncementAdminService {
    private final AnnouncementAdminRepository announcementAdminRepository;

    @Transactional
    public List<AnnouncementAdminDto> findAllAnnouncement() {
        List<AnnouncementAdmin> announcements = announcementAdminRepository.findAllByIsDelFalse().orElse(null);
        List<AnnouncementAdminDto> adminDtoList = null;
        if (announcements != null) {
            adminDtoList = announcements.stream().map(AnnouncementAdminDto::from).collect(Collectors.toList());
        }
        return adminDtoList;
    }
}
