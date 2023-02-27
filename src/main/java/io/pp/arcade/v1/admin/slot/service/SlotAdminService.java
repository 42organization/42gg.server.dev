package io.pp.arcade.v1.admin.slot.service;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import io.pp.arcade.v1.admin.slot.dto.SlotAdminResponseDto;
import io.pp.arcade.v1.admin.slot.repository.SlotManagementRepository;
import io.pp.arcade.v1.global.scheduler.SlotGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SlotAdminService {
    private final SlotManagementRepository slotManagementRepository;
    private final SlotGenerator slotGenerator;
    @Transactional(readOnly = true)
    public SlotAdminResponseDto getSlotSetting() {
        SlotManagement slotManagement = slotManagementRepository.findFirstByOrderByIdDesc();
        if (slotManagement == null) {
            return null;
        }
        return new SlotAdminResponseDto(slotManagement);
    }

    public void setSlotGenerator(Integer interval, Integer futureSlotTime){
        SlotManagement slotManagement = slotManagementRepository.findFirstByOrderByIdDesc();
        if (slotManagement == null) {
            slotGenerator.setFutureTimeGap(futureSlotTime);
        }
        else {
            slotGenerator.setFutureTimeGap(slotGenerator.getFutureTimeGap()
                    + futureSlotTime - slotManagement.getFutureSlotTime());
        }
        slotGenerator.setInterval(interval);
    }

    public void addSlotSetting(Integer pastSlotTime, Integer futureSlotTime,
                               Integer interval, Integer openMinute) {
        SlotManagement slotManagement = SlotManagement.builder()
                .futureSlotTime(futureSlotTime)
                .pastSlotTime(pastSlotTime)
                .interval(interval)
                .openMinute(openMinute)
                .build();
        slotManagementRepository.save(slotManagement);
    }
}
