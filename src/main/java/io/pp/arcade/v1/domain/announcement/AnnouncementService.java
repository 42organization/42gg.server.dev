package io.pp.arcade.v1.domain.announcement;

import io.pp.arcade.v1.domain.announcement.dto.AnnouncementDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Transactional
    public AnnouncementDto findLastAnnouncement() {
        Announcement announcement = announcementRepository.findFirstByOrderByIdDesc();
        if (announcement != null && announcement.getIsDel() == false) {
            AnnouncementDto dto = AnnouncementDto.from(announcement);
            return dto;
        }
        return null;
    }
}
