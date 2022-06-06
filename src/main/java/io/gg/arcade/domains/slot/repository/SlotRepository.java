package io.gg.arcade.domains.slot.repository;

import io.gg.arcade.domains.slot.entity.Slot;

import java.util.List;

public interface SlotRepository {
    Slot save(Slot slot);
    Slot findById(Integer slotId);
    List<Slot> findAll();
}
