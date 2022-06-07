package io.gg.arcade.domain.slot.controller;

import io.gg.arcade.domain.slot.dto.SlotRequestDto;
import io.gg.arcade.domain.slot.entity.Slot;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SlotController {
    List<Slot> findSlots(SlotRequestDto slotRequestDto, @RequestParam String type);
}
