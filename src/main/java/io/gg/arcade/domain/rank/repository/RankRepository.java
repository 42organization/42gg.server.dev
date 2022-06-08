package io.gg.arcade.domain.rank.repository;

import io.gg.arcade.domain.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    Rank findByUserId(Integer userId);
}
