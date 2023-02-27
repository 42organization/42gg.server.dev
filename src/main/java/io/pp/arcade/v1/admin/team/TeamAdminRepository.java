package io.pp.arcade.v1.admin.team;

import io.pp.arcade.v1.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAdminRepository extends JpaRepository<Team, Integer> {
}
