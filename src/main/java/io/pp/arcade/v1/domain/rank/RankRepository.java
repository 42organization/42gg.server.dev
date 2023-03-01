package io.pp.arcade.v1.domain.rank;


import io.pp.arcade.v1.domain.rank.entity.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    Page<Rank> findAllBySeasonId(Integer seasonId, Pageable pageable);
    Optional<Rank> findBySeasonIdAndUserId(Integer seasonId, Integer userId);
    Page<Rank> findAllByOrderByIdDesc(Pageable pageable);
    List<Rank> findAllBySeasonId(Integer seasonId);
    void deleteAllBySeasonId(Integer seasonId);
}
