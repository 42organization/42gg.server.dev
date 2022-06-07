package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.*;
import io.gg.arcade.domain.slot.entity.Slot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SlotService {

    void addTodaySlots();
    public void addSlot(SlotAddRequestDto dto);
    void addUserInSlot(SlotRequestDto slotDto);
    void removeUserInSlot(SlotRequestDto slotDto);
    List<SlotResponseDto> filterSlots(SlotFindDto slotFindDto);
}
