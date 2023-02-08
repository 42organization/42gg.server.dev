package io.pp.arcade.v1.admin.announcement.repository;

import io.pp.arcade.v1.admin.announcement.AnnouncementAdmin;
import io.pp.arcade.v1.domain.announcement.Announcement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementAdminRepository extends JpaRepository<AnnouncementAdmin, Integer> {
    List<AnnouncementAdmin> findAll(Sort sort);
    Optional<List<AnnouncementAdmin>> findAllByIsDelFalse();
    Optional<AnnouncementAdmin> findByIsDelFalse();

    //페이징 처리하기
    //Optional<List<AnnouncementAdmin> findAll();
    // Page<AnnouncementAdmin> findAllByOrderByIdDesc(Pageable pageable);
}
