package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.GameAllDto;
import io.pp.arcade.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.domain.admin.dto.delete.GameDeleteDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class GameAdminControllerImpl implements GameAdminController {
    @Override
    @PostMapping(value = "/game")
    public void gameCreate(GameCreateDto gameCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/game/{id}")
    public void gameUpdate(Integer id, GameUpdateDto gameUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/game/{id}")
    public void gameDelete(Integer id, GameDeleteDto gameDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/game")
    public void gameAll(Pageable pageable, HttpServletRequest request) {

    }
}
