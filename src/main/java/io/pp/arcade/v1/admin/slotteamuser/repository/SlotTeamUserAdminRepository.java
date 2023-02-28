package io.pp.arcade.v1.admin.slotteamuser.repository;

import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SlotTeamUserAdminRepository extends JpaRepository<SlotTeamUser, Integer> {
    @Query("select stu from SlotTeamUser stu join fetch stu.slot join fetch stu.user join fetch stu.team where stu.slot = :s")
    List<SlotTeamUser> findAllBySlot(@Param("s") Optional<Slot> slot);   //슬롯으로 SlotTeamUser 검색
}
