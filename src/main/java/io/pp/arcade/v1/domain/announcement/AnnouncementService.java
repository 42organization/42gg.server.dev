package io.pp.arcade.v1.domain.announcement;

import io.pp.arcade.v1.domain.announcement.dto.AnnouncementAddRequestDto;
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
    public List<AnnouncementDto> findAllAnnouncement() {
        List<Announcement> announcements = announcementRepository.findAllByIsDelFalse().orElse(null);
        List<AnnouncementDto> dtoList = null;
        if (announcements != null) {
            dtoList = announcements.stream().map(AnnouncementDto::from).collect(Collectors.toList());
        }
        return dtoList;
    }
}
