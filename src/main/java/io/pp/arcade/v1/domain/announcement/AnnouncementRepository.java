package io.pp.arcade.v1.domain.announcement;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<AnnouncementAdmin, Integer> {
    Optional<List<AnnouncementAdmin>> findAllByIsDelFalse();
}
