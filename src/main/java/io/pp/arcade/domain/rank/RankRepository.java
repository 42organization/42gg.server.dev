package io.pp.arcade.domain.rank;


import io.pp.arcade.global.util.RacketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    Rank findByUserId(Integer userId);
    Rank findByUserIdAndRacketType(Integer userId, RacketType type);
}
