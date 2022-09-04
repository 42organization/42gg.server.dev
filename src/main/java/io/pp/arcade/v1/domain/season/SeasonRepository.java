package io.pp.arcade.v1.domain.season;

import io.pp.arcade.v1.global.type.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository <Season, Integer> {
    Optional<Season> findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime startTime, LocalDateTime endTime);
    Optional<Season> findFirstByOrderByIdDesc();
    List<Season> findAllBySeasonMode(Mode mode);
    Page<Season> findAllByOrderByIdDesc(Pageable pageable);
}
