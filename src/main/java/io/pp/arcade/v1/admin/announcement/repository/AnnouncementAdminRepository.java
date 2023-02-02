package io.pp.arcade.v1.admin.announcement.repository;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.domain.announcement.Announcement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementAdminRepository extends JpaRepository<AnnouncementAdmin, Integer> {
    Optional<List<AnnouncementAdmin>> findAllByIsDelFalse();
}
