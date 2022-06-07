package io.gg.arcade.domain.team.repository;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    void deleteByUserAndTeamId(User user, String teamId);
}
