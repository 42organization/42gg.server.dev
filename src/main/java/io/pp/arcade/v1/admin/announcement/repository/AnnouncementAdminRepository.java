package io.pp.arcade.v1.admin.announcement.repository;

import io.pp.arcade.v1.domain.announcement.Announcement;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementAdminRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findAll(Sort sort);

    Announcement findFirstByOrderByIdDesc();
}
