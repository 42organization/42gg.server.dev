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
            return null;
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

//        System.out.println(requestDto.getInterval());
//        System.out.println(requestDto.getFutureTime());
//        System.out.println(requestDto.getPastTime());
//        System.out.println(requestDto.getRevealTime());
//        slotAdminService.test();

//        slotAdminService.test();
//        slotAdminService.setSlotGenerator();
//        slotAdminService.test();
//        System.out.println("test put");
        return ResponseEntity.ok().build();
    }

    private void checkValid(SlotAdminRequestDto requestDto) {
        List<Integer> intervals = List.of(15, 30, 60);
        if (requestDto.getInterval() == null
                || intervals.contains(requestDto.getInterval())) {
            throw new BusinessException("SC001");
        }
        if (requestDto.getFutureTime() == null
                || requestDto.getFutureTime() > 12) {
            throw new BusinessException("SC001");
        }
        if (requestDto.getPastTime() == null
                || requestDto.getPastTime() > 12) {
            throw new BusinessException("SC001");
        }
        if (requestDto.getRevealTime() == null
                || requestDto.getRevealTime() > requestDto.getInterval()) {
            throw new BusinessException("SC001");
        }
    }
}
