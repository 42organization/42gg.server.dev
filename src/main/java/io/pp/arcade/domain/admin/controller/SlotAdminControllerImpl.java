package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.SlotAllDto;
import io.pp.arcade.domain.admin.dto.create.SlotCreateDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class SlotAdminControllerImpl implements SlotAdminController {
    @Override
    @PostMapping(value = "/slot")
    public void slotCreate(SlotCreateDto slotCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/slot/{id}")
    public void slotUpdate(SlotUpdateDto slotUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/slot/{id}")
    public void slotDelete(SlotDeleteDto slotDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/slot")
    public void slotAll(SlotAllDto slotAllDto, HttpServletRequest request) {

    }
}
