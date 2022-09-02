package io.pp.arcade.v1.domain.slotteamuser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotTeamUserRepository extends JpaRepository<SlotTeamUser, Integer> {
    List<SlotTeamUser> findAllByTeamId(Integer teamId);
    List<SlotTeamUser> findAllByUserId(Integer userId);
    List<SlotTeamUser> findAllBySlotId(Integer slotId);
    List<SlotTeamUser> findAllBySlotIdOrderByTeam(Integer slotId);
    Optional<SlotTeamUser> findSlotTeamUserBySlotIdAndUserId(Integer slotId, Integer userId);
    Optional<SlotTeamUser> findByTeamIdAndUserId(Integer teamId, Integer userId);

    Page<SlotTeamUser> findAllByOrderByTeamUserId(Pageable pageable);
}
