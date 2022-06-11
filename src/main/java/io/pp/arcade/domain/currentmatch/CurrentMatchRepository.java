package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentMatchRepository extends JpaRepository<CurrentMatch, Integer> {
    CurrentMatch findByUser(User user);
}
