package io.pp.arcade.v1.admin.game.repository;

import io.pp.arcade.v1.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameAdminRepository extends JpaRepository<Game, Integer> {
}
