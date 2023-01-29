package io.pp.arcade.v1.domain.slotteamuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeletedSlotTeamUserRepository extends JpaRepository<DeletedSlotTeamUser, Integer> {
}
