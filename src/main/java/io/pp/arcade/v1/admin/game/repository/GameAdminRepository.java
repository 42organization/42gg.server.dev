package io.pp.arcade.v1.admin.game.repository;

import io.pp.arcade.v1.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameAdminRepository extends JpaRepository<Game, Integer> {
    List findBySeason(int seasonId);
}
