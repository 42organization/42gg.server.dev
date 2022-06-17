package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.SlotCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateRequestDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class SlotAdminControllerImpl implements SlotAdminController {
    private final SlotService slotService;

    @Override
    @PostMapping(value = "/slot")
    public void slotCreate(SlotCreateRequestDto createDto, HttpServletRequest request) {
        slotService.createSlotByAdmin(createDto);
    }

    @Override
    @PutMapping(value = "/slot/{id}")
    public void slotUpdate(Integer id, SlotUpdateRequestDto slotUpdateDto, HttpServletRequest request) {
        slotUpdateDto = SlotUpdateRequestDto.builder()
                .id(id).build();
        slotService.updateSlotByAdmin(slotUpdateDto);
    }

    @Override
    @DeleteMapping(value = "/slot/{id}")
    public void slotDelete(Integer id, HttpServletRequest request) {
        SlotDeleteDto deleteDto = SlotDeleteDto.builder()
                .id(id).build();
        slotService.deleteSlotByAdmin(deleteDto);
    }

    @Override
    @GetMapping(value = "/slot")
    public void slotAll(Pageable pageable, HttpServletRequest request) {
        List<SlotDto> slotDtos = slotService.findSlotByAdmin(pageable);
    }

}
