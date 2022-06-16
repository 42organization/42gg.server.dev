package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.SlotAllDto;
import io.pp.arcade.domain.admin.dto.create.SlotCreateDto;
import io.pp.arcade.domain.admin.dto.delete.SlotDeleteDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface SlotAdminController {
    void slotCreate(SlotCreateDto slotCreateDto, HttpServletRequest request);
    void slotUpdate(@PathVariable Integer id, SlotUpdateDto slotUpdateDto, HttpServletRequest request);
    void slotDelete(@PathVariable Integer id, SlotDeleteDto slotDeleteDto, HttpServletRequest request);
    void slotAll(Pageable pageable, HttpServletRequest request);
}
