package io.pp.arcade.v1.admin.slot.repository;

import io.pp.arcade.v1.domain.slot.Slot;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlotAdminRepository extends JpaRepository<Slot, Integer> {
    Optional<Slot> findById(int slotId);
}
