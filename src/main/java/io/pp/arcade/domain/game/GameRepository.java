package io.pp.arcade.domain.game;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findBySlot(Slot slot);
    Page<Game> findByStatus(String status, Pageable pageable);
    Page<Game> findByIdLessThanAndStatusOrderByIdDesc(Integer id, StatusType status, Pageable pageable);
    Page<Game> findByIdLessThanOrderByIdDesc (Integer id, Pageable pageable);
}
