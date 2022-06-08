package io.gg.arcade.domain.slot.repository;

import io.gg.arcade.domain.slot.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findAllByCreatedDateIsAfter(LocalDateTime time);
}
