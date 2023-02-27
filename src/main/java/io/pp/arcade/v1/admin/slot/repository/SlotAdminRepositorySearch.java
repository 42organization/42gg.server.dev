package io.pp.arcade.v1.admin.slot.repository;

import io.pp.arcade.v1.domain.slot.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface SlotAdminRepositorySearch {
    Page<Slot> findSlotByGameId(@Param("gameId")Integer slotId, Pageable pageable);
}
