package io.gg.arcade.domain.slot.controller;

import io.gg.arcade.domain.slot.dto.SlotModifyRequestDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.user.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SlotController {
    List<SlotResponseDto> findSlots(@PathVariable Integer tableId, @RequestParam String type, @RequestParam Integer userId);
    void addUserInSlot(@PathVariable Integer tableId, @RequestBody SlotModifyRequestDto slotRequestDto, @RequestParam Integer userId);
    void removeUserInSlot(@RequestParam Integer matchId);
}
