package io.gg.arcade.domains.slot.repository;

import io.gg.arcade.domains.slot.entity.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemorySlotRepository implements SlotRepository {
    private static Map<Integer, Slot> store = new HashMap<>();
    private static int sequence = 0;

    public Slot save(Slot slot) {
        slot.setSlotId(++sequence);
        store.put(slot.getSlotId(), slot);
        return slot;
    }

    public Slot findById(Integer slotId){
        return store.get(slotId);
    }

    public List<Slot> findAll(){
        return new ArrayList<Slot>(store.values());
    }
}
