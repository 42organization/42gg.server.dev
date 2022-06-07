package io.gg.arcade.domain.game.repository;

import io.gg.arcade.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
