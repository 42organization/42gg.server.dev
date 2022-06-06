package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;

import java.time.LocalDateTime;

public interface SlotService {

    void addTodaySlots();
    Slot addSlot(LocalDateTime time);
    void addUserInSlot(SlotRequestDto slotDto);
    void removeUserInSlot(SlotRequestDto slotDto);
}
