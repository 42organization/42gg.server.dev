package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.CurrentMatchAllDto;
import io.pp.arcade.domain.admin.dto.create.CurrentMatchCreateDto;
import io.pp.arcade.domain.admin.dto.delete.CurrentMatchDeleteDto;
import io.pp.arcade.domain.admin.dto.update.CurrentMatchUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface CurrentMatchAdminController {
    void currentMatchCreate(CurrentMatchCreateDto currentMatchCreateDto, HttpServletRequest request);
    void currentMatchUpdate(CurrentMatchUpdateDto currentMatchUpdateDto, HttpServletRequest request);
    void currentMatchDelete(CurrentMatchDeleteDto currentMatchDeleteDto, HttpServletRequest request);
    void currentMatchAll(CurrentMatchAllDto currentMatchAllDto, HttpServletRequest request);
}
