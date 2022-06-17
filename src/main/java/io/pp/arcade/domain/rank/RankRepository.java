package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.RacketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    Page<Rank> findAll(Pageable pageablxe);
    Rank findByUserId(Integer userId);
    Rank findByUserIdAndRacketType(Integer userId, RacketType type);
}
