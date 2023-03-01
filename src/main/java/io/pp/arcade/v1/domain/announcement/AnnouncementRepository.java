package io.pp.arcade.v1.domain.announcement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    Announcement findFirstByOrderByIdDesc();
}
