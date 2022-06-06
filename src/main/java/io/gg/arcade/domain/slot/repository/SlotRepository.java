package io.gg.arcade.domain.slot.repository;

import io.gg.arcade.domain.slot.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
}
