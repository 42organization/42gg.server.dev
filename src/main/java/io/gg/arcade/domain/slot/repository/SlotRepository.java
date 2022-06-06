package io.gg.arcade.domain.slot.repository;

import io.gg.arcade.domain.slot.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findAllByCreatedDateAfter(LocalDateTime localDateTime);
}