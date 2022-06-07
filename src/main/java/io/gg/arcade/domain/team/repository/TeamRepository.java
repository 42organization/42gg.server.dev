package io.gg.arcade.domain.team.repository;

import io.gg.arcade.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
