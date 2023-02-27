package io.pp.arcade.v1.admin.slot.service;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import io.pp.arcade.v1.admin.slot.dto.SlotAdminResponseDto;
import io.pp.arcade.v1.admin.slot.dto.SlotPolicy;
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
    @Transactional(readOnly = true)
    public SlotAdminResponseDto getSlotSetting() {
        SlotManagement slotManagement = slotManagementRepository.findFirstByOrderByCreatedAtDesc();
        if (slotManagement == null) {
            return null;
        }
        return new SlotAdminResponseDto(slotManagement);
    }

    @Transactional(readOnly = true)
    public SlotPolicy getNowSlotPolicy() {
        SlotManagement nowSlotPolicy = slotManagementRepository.findFirstByOrderByCreatedAtDesc();
        if (nowSlotPolicy != null)
            return new SlotPolicy(nowSlotPolicy.getGameInterval(), nowSlotPolicy.getFutureSlotTime(),
                    nowSlotPolicy.getPastSlotTime());
        else
            return new SlotPolicy(15, 9, 9); //return default policy
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
