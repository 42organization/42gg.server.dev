package io.pp.arcade.v1.admin.game.repository;

import io.pp.arcade.v1.domain.game.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameAdminRepository extends JpaRepository<Game, Integer> {
    @Override
    @Query("select g from Game g join fetch g.slot order by g.id desc")
    List<Game> findAll();

    List<Game> findBySeason(int seasonId);
}
