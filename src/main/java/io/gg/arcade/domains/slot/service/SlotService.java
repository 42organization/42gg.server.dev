package io.gg.arcade.domains.slot.service;

import io.gg.arcade.domains.slot.dto.SlotDto;
import io.gg.arcade.domains.slot.entity.Slot;

import java.util.List;

public interface SlotService {
    List<Slot> findAll();
    Slot findSlot(SlotDto slotDto);
    Slot saveSlot(SlotDto slotDto);
}
