package io.pp.arcade.v1.domain.opponent;

import io.pp.arcade.v1.domain.opponent.Opponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpponentRepository extends JpaRepository<Opponent, Integer> {

    Optional<Opponent> findByIntraId(String intraId);
}
