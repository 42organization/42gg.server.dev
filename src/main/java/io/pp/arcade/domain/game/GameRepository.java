package io.pp.arcade.domain.game;

import io.pp.arcade.domain.slot.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findBySlot(Slot slot);
    List<Game> findAllByStatus(String status);
}
