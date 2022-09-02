package io.pp.arcade.v1.domain.currentmatch;

import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrentMatchRepository extends JpaRepository<CurrentMatch, Integer> {
    Optional<CurrentMatch> findByUserAndIsDel(User user, Boolean isDel);
    Optional<CurrentMatch> findByUserIdAndIsDel(Integer userId, Boolean isDel);

    List<CurrentMatch> findAllByGameId(Integer gameId);
    List<CurrentMatch> findAllBySlotId(Integer slotId);
    Page<CurrentMatch> findAllByOrderByIdDesc(Pageable pageable);
    List<CurrentMatch> findAllByIsDel(Boolean isDel);
    void deleteByUser(User user);
}
