package io.gg.arcade.domain.pchange.repository;

import io.gg.arcade.domain.pchange.entity.PChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PchangeRepository extends JpaRepository<PChange, Integer> {
    List<PChange> findByUserId(Integer userId);
    PChange findByUserIdAndGameId(Integer userId, Integer GameId);
}
