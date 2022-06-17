package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.GameCreateRequestDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface GameAdminController {
    void gameCreate(@RequestBody GameCreateRequestDto gameCreateDto, HttpServletRequest request);
    void gameUpdate(@PathVariable Integer id, GameUpdateDto gameUpdateDto, HttpServletRequest request);
    void gameDelete(@PathVariable Integer id, HttpServletRequest request);
    void gameAll(Pageable pageable, HttpServletRequest request);
}
