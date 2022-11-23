package io.pp.arcade.v1.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpponentRepository extends JpaRepository<Integer, Opponent> {
    @Query(nativeQuery = true, value = "select o from Opponent o where o.isReady == true")
    List<Opponent> findAllByIsReady();
}
