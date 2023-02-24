package io.pp.arcade.v1.admin.season.repository;

import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.global.type.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeasonAdminRepository extends JpaRepository <Season, Integer> {

    /* 입력시간과 모드로 입력시간이 속한 시즌 가져오기 */
    @Query("SELECT e FROM Season e WHERE e.seasonMode = :mode AND e.startTime < :targetTime AND e.endTime > :targetTime ORDER BY e.id DESC")
    Season findFirstByModeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByCreateTimeDesc(@Param("mode") Mode mode, @Param("targetTime") LocalDateTime targetTime);

    /* 입력시간과 모드로 입력시간 이후 가장 가까운 시즌 가져오기 */
    @Query("SELECT e FROM Season e WHERE e.seasonMode = :mode AND e.startTime >= :targetTime ORDER BY e.startTime ASC")
    Season findNearestAfterSeason(@Param("mode") Mode seasonMode, @Param("targetTime") LocalDateTime targetTime);
    Optional<Season> findSeasonByAndStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime startTime, LocalDateTime endTime);
    Optional<Season> findFirstByOrderByIdDesc();
    Optional<Season> findFirstBySeasonModeOrSeasonModeOrderByIdDesc(Mode mode1, Mode mode2);
    List<Season> findAllBySeasonMode(Mode mode);
    List<Season> findAllBySeasonModeOrSeasonMode(Mode mode1, Mode mode2);
    List<Season> findAllBySeasonModeOrSeasonModeAndStartTimeIsBeforeOrderByStartTimeDesc(Mode mode1, Mode mode2, LocalDateTime now);
    Page<Season> findAllByOrderByIdDesc(Pageable pageable);
}
