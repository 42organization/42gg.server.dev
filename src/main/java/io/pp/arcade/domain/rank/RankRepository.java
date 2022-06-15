package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RankRepository <Rank, Integer> {
    Page<Rank> findRankList(Pageable pageable);
}
