package io.pp.arcade.v1.domain.opponent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpponentRepository extends JpaRepository<Opponent, Integer> {
    List<Opponent> findAllByIsReady(boolean isReady);
    Optional<Opponent> findByIntraId(String intraId);
}
