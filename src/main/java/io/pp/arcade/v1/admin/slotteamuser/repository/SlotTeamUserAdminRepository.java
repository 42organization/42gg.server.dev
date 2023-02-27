package io.pp.arcade.v1.admin.slotteamuser.repository;

import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotTeamUserAdminRepository extends JpaRepository<SlotTeamUser, Integer> {

    //@Query(nativeQuery = true, value = "select s from SlotTeamUser as s where s.slot.id =: slotId)

    List<SlotTeamUser> findAllBySlot(Optional<Slot> slot);  //슬롯으로 SlotTeamUser 검색
}
