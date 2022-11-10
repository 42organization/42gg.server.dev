package io.pp.arcade.v1.domain.pchange;

import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.Mode;
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

    @Query(nativeQuery = true, value = "SELECT * FROM pchange " +
            "where game_id in (SELECT id FROM game where season = :season and mode = :mode ) " +
            "AND user_id = :intraId ORDER BY id Desc limit :limit")
    List<PChange> findPChangeHistory(@Param("intraId") String intraId,
                                     @Param("season") Integer season,
                                     @Param("mode") Integer mode, @Param("limit") Integer limit);
    /* status가 Entitiy에 없어요 ㅠㅠ */
    //     Page<PChange> findAllByUserAndStatusAndGameIdLessThanOrderByIdDesc(User user, String status, Integer gameId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM pchange " +
            "where game_id in " +
            "(SELECT id FROM game where season like if(:season is NULL, '%', :season) " +
            "and id < :gameId AND mode like if(:mode is NULL, '%', :mode)) " +
            "AND user_id = :intraId ORDER BY id Desc " +
            "limit :limit")
    List<PChange> findPChangesByGameModeAndUser(@Param("season") Integer season,
                                                @Param("mode") Integer mode,
                                                @Param("intraId") String intraId,
                                                @Param("gameId") Integer gameId,
                                                @Param("limit") Integer limit);

    Page<PChange> findPChangesByGame_ModeAndUser_IntraIdAndGameIdLessThanOrderByIdDesc(Mode mode, String intraId, Integer gameId, Pageable pageable);

    Page<PChange> findAllByUser_IntraIdAndGame_ModeAndGameIdLessThanOrderByIdDesc(String intraId, Mode mode, Integer gameId, Pageable pageable);

//    @Query(nativeQuery = true, value = "SELECT * FROM pchange where game_id = :gameId AND user_id = :intraId")
    Optional<PChange> findByUser_IntraIdAndGame_Id(@Param("intraId") String intraId, @Param("gameId")Integer gameId);

    Page<PChange> findAllByOrderByIdDesc(Pageable pageable);
}
