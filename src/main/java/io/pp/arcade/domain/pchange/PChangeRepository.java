package io.pp.arcade.domain.pchange;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PChangeRepository extends JpaRepository<PChange, Integer> {
    Optional<List<PChange>> findAllByGameId(Integer gameId);
    List<PChange> findAllByUserIdOrderByIdDesc(Integer userId);
}
