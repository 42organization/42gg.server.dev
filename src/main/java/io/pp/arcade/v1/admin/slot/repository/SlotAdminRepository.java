package io.pp.arcade.v1.admin.slot.repository;

import io.pp.arcade.v1.domain.slot.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotAdminRepository extends JpaRepository<Slot, Integer> {
}
