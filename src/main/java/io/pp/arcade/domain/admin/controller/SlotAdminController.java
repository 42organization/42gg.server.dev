package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.SlotCreateRequestDto;
import io.pp.arcade.domain.admin.dto.update.SlotUpdateRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface SlotAdminController {
    void slotCreate(@RequestBody SlotCreateRequestDto createDto, HttpServletRequest request);
    void slotUpdate(@PathVariable Integer id, SlotUpdateRequestDto updateDto, HttpServletRequest request);
    void slotDelete(@PathVariable Integer id, HttpServletRequest request);
    void slotAll(Pageable pageable, HttpServletRequest request);
}
