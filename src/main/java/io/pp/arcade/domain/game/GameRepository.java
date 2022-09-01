package io.pp.arcade.domain.game;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findBySlot(Slot slot);
    Optional<Game> findBySlotId(Integer slotId);
    Page<Game> findAllByTypeOrderByIdDesc(Pageable pageable, GameType type);


//    @Query(nativeQuery = true,
//            value = "select * from game where id < :gameId and status is :status and mode is :mode and season is :seasonId ORDER BY id DESC",
//            countQuery = "SELECT count(*) FROM game")
//    Page<Game> findGameListOrderByIdDesc(@Param("seasonId")Integer seasonId, @Param("gameId")Integer gameId, @Param("mode")Integer mode, @Param("status")Integer status, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from game where id < :gameId and status LIKE IF (:status is NULL, '%', :status) and mode LIKE IF(:mode is NULL, '%', :mode) and season LIKE IF(:seasonId is NULL, '%', :seasonId) ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM game")
    Page<Game> findGameListOrderByIdDesc(@Param("seasonId")Integer seasonId, @Param("gameId")Integer gameId, @Param("mode")Integer mode, @Param("status")Integer status, Pageable pageable);

    Page<Game> findByStatus(String status, Pageable pageable);

    Page<Game> findByIdLessThanAndStatusOrderByIdDesc(Integer id, StatusType status, Pageable pageable);

    Page<Game> findByIdLessThanOrderByIdDesc (Integer id, Pageable pageable);

    Page<Game> findByIdLessThanAndStatusAndModeOrderByIdDesc(Integer id, StatusType status, Mode mode, Pageable pageable);

    Page<Game> findByIdLessThanAndModeOrderByIdDesc(Integer id, Mode mode, Pageable pageable);
}
