package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.SlotCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateDto;
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
    public void slotCreate(SlotCreateRequestDto createRequestDto, HttpServletRequest request) {
        slotService.createSlotByAdmin(createRequestDto);
    }

    @Override
    @PutMapping(value = "/slot")
    public void slotUpdate(SlotUpdateRequestDto updateRequestDto, HttpServletRequest request) {
        SlotUpdateDto updateDto = SlotUpdateDto.builder()
                .slotId(updateRequestDto.getSlotId())
                .type(updateRequestDto.getType())
                .headCount(updateRequestDto.getHeadCount())
                .gamePpp(updateRequestDto.getGamePpp())
                .build();
        slotService.updateSlotByAdmin(updateDto);
    }

    @Override
    @DeleteMapping(value = "/slot/{id}")
    public void slotDelete(Integer slotId, HttpServletRequest request) {
        SlotDeleteDto deleteDto = SlotDeleteDto.builder()
                .id(slotId).build();
        slotService.deleteSlotByAdmin(deleteDto);
    }

    @Override
    @GetMapping(value = "/slot")
    public List<SlotDto> slotAll(Pageable pageable, HttpServletRequest request) {
        List<SlotDto> slotDtos = slotService.findSlotByAdmin(pageable);
        return slotDtos;
    }

}
