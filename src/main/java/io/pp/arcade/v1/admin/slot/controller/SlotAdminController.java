package io.pp.arcade.v1.admin.slot.controller;

import io.pp.arcade.v1.admin.slot.dto.SlotAdminRequestDto;
import io.pp.arcade.v1.admin.slot.dto.SlotAdminResponseDto;
import io.pp.arcade.v1.admin.slot.service.SlotAdminService;
import io.pp.arcade.v1.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pingpong/admin/slot")
public class SlotAdminController {
    private final SlotAdminService slotAdminService;
    @GetMapping
    public SlotAdminResponseDto getSlotSetting(HttpResponse httpResponse) {
        SlotAdminResponseDto responseDto = slotAdminService.getSlotSetting();
        if (responseDto == null) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            throw new BusinessException("SR001");
        }
        return responseDto;
    }
    @PutMapping
    public ResponseEntity modifySlotSetting(@RequestBody SlotAdminRequestDto requestDto){
        try {
            checkValid(requestDto);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        slotAdminService.addSlotSetting(requestDto.getPastSlotTime(),
                requestDto.getFutureSlotTime(),
                requestDto.getInterval(),
                requestDto.getOpenMinute());
        return ResponseEntity.ok().build();
    }

    private void checkValid(SlotAdminRequestDto requestDto) {
        if (requestDto.getInterval() == null
                || 60 % requestDto.getInterval() != 0) {
            throw new BusinessException("SM001");
        }
        if (requestDto.getFutureSlotTime() == null
                || requestDto.getFutureSlotTime() > 23) {
            throw new BusinessException("SM001");
        }
        if (requestDto.getPastSlotTime() == null
                || requestDto.getPastSlotTime() > 3) {
            throw new BusinessException("SM001");
        }
        if (requestDto.getOpenMinute() == null
                || requestDto.getOpenMinute() > requestDto.getInterval()) {
            throw new BusinessException("SM001");
        }
    }
}
