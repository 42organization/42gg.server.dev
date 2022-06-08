package io.gg.arcade.domain.team.repository;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    void deleteByUserAndTeamId(User user, String teamId);
    List<Team> findTeamByTeamIdOrTeamId(String team1Id, String team2Id);
    List<Team> findByTeamId(String teamId);
    @Query(nativeQuery = true, value = "select * from team where user_id == :userId")
    String findTeamIdByUserId(@Param("userId") String userId);
}
