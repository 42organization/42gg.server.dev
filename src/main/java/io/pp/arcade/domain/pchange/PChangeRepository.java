package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.statistic.TableMapper;
import io.pp.arcade.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PChangeRepository extends JpaRepository<PChange, Integer> {
    Optional<List<PChange>> findAllByGame(Game game);
    Optional<List<PChange>> findAllByUserIntraId(String intraId);
    Page<PChange> findAllByUser(User user, Pageable pageable);
    Page<PChange> findAllByUserOrderByIdDesc(User user, Pageable pageable);
    /* status가 Entitiy에 없어요 ㅠㅠ */
    //     Page<PChange> findAllByUserAndStatusAndGameIdLessThanOrderByIdDesc(User user, String status, Integer gameId, Pageable pageable);

    Page<PChange> findAllByUserAndGameIdLessThanOrderByIdDesc(User user, Integer gameId, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from pchange where game_id in (select id as game_id from game where mode = :mode and id < :gameId) and user_id = :intraId order by id desc")
    Page<PChange> findPChangeByGameMode(@Param("intraId") String intraId, @Param("mode") Integer mode, @Param("gameId") Integer gameId, Pageable pageable);

    Page<PChange> findAllByUser_IntraIdAndGame_ModeAndGameIdLessThanOrderByIdDesc(String intraId, Mode mode, Integer gameId, Pageable pageable);

    Optional<PChange> findByUserAndGame(User user, Game game);

    Page<PChange> findAllByOrderByIdDesc(Pageable pageable);
}
