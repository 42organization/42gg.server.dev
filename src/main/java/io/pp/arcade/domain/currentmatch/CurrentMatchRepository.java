package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrentMatchRepository extends JpaRepository<CurrentMatch, Integer> {
    Optional<CurrentMatch> findByUser(User user);
    Optional<CurrentMatch> findByUserId(Integer userId);
    List<CurrentMatch> findAllByGameId(Integer gameId);
    List<CurrentMatch> findAllBySlotId(Integer slotId);
    Page<CurrentMatch> findAllByOrderByIdDesc(Pageable pageable);
    void deleteByUser(User user);
}
