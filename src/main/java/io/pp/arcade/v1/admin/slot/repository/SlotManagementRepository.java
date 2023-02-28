package io.pp.arcade.v1.admin.slot.repository;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotManagementRepository
    extends JpaRepository<SlotManagement, Integer> {
    SlotManagement findFirstByOrderByIdDesc();
    SlotManagement findFirstByOrderByCreatedAtDesc();
}
