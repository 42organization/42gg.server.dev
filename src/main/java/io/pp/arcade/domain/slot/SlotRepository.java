package io.pp.arcade.domain.slot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findAllByCreatedDateAfter(LocalDateTime time);
}
