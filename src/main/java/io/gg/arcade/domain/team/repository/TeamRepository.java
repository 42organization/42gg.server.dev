package io.gg.arcade.domain.team.repository;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByTeamId(String teamId);
}
