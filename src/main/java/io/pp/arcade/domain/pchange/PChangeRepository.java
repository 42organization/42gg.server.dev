package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PChangeRepository extends JpaRepository<PChange, Integer> {
    Optional<List<PChange>> findAllByGame(Game game);
    Page<PChange> findAllByUserOrderByIdDesc(User user, Pageable pageable);
    Optional<PChange> findByUserAndGame(User user, Game game);
}
