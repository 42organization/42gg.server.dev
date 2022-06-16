package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.GameAllDto;
import io.pp.arcade.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.domain.admin.dto.delete.GameDeleteDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface GameAdminController {
    void gameCreate(GameCreateDto gameCreateDto, HttpServletRequest request);
    void gameUpdate(@PathVariable Integer id, GameUpdateDto gameUpdateDto, HttpServletRequest request);
    void gameDelete(@PathVariable Integer id, GameDeleteDto gameDeleteDto, HttpServletRequest request);
    void gameAll(Pageable pageable, HttpServletRequest request);
}
