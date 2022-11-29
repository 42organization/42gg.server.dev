package io.pp.arcade.v1.domain.rank;

import io.pp.arcade.v1.domain.rank.entity.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    Page<Rank> findAllBySeasonId(Integer seasonId, Pageable pageable);
    Optional<Rank> findBySeasonIdAndUserId(Integer seasonId, Integer userId);
    Page<Rank> findAllByOrderByIdDesc(Pageable pageable);

    List<Rank> findAllBySeasonId(Integer seasonId);

    @Query(nativeQuery = true, value = "select ranking from (select user_id, row_number() over (order by ppp desc) as ranking from ranks) ranked where user_id=:intraId")
    Integer findRankingByIntraId(@Param("intraId")String intraId);

    Page<Rank> findAllByOrderByPppDesc(Pageable pageable);
}
