package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.GameAllDto;
import io.pp.arcade.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.domain.admin.dto.delete.GameDeleteDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface GameAdminController {
    void gameCreate(GameCreateDto gameCreateDto, HttpServletRequest request);
    void gameUpdate(GameUpdateDto gameUpdateDto, HttpServletRequest request);
    void gameDelete(GameDeleteDto gameDeleteDto, HttpServletRequest request);
    void gameAll(GameAllDto gameAllDto, HttpServletRequest request);
}
