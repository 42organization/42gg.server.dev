package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.SlotAllDto;
import io.pp.arcade.domain.admin.dto.create.SlotCreateDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface SlotAdminController {
    void slotCreate(SlotCreateDto slotCreateDto, HttpServletRequest request);
    void slotUpdate(SlotUpdateDto slotUpdateDto, HttpServletRequest request);
    void slotDelete(SlotDeleteDto slotDeleteDto, HttpServletRequest request);
    void slotAll(SlotAllDto slotAllDto, HttpServletRequest request);
}
