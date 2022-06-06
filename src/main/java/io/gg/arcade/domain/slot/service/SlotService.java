package io.gg.arcade.domain.slot.service;

import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.dto.SlotListResponseDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.slot.entity.Slot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SlotService {

    void addTodaySlots();
    Slot addSlot(LocalDateTime time);
    void addUserInSlot(SlotRequestDto slotDto);
    void removeUserInSlot(SlotRequestDto slotDto);
    List<SlotResponseDto> findByDate(SlotFindDto slotFindDto);
}
