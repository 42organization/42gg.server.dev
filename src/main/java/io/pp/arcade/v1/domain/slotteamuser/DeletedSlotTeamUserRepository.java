package io.pp.arcade.v1.domain.slotteamuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeletedSlotTeamUserRepository extends JpaRepository<DeletedSlotTeamUser, Integer> {
    List<DeletedSlotTeamUser> findAllByTeamId(Integer teamId);
    List<DeletedSlotTeamUser> findAllByUserId(Integer userId);
    Optional<DeletedSlotTeamUser> findByTeamAndUser(Integer teamId, Integer userId);
}
