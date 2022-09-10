package io.pp.arcade.v1.domain.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Page<Team> findAllByOrderByIdDesc(Pageable pageable);
    List<Team> findAllBySlotId(Integer slotId);
}
