package io.gg.arcade.domains.slot.service;

import io.gg.arcade.domains.slot.dto.SlotDto;
import io.gg.arcade.domains.slot.entity.Slot;
import io.gg.arcade.domains.slot.repository.MemorySlotRepository;
import io.gg.arcade.domains.slot.repository.SlotRepository;

import java.util.List;

public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository = new MemorySlotRepository();

    @Override
    public List<Slot> findAll() {
        return slotRepository.findAll();
    }

    @Override
    public Slot findSlot(SlotDto slotDto) {
        return slotRepository.findById(slotDto.getSlotId());
    }

    @Override
    public Slot saveSlot(SlotDto slotDto) {
        return slotRepository.save(slotDto.toEntity());
    }
}
