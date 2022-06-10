package io.pp.arcade.domain.game.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository <Game, Integer> {
}
