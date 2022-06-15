package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.RankAllDto;
import io.pp.arcade.domain.admin.dto.create.RankCreateDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class RankAdminControllerImpl implements RankAdminController {
    @Override
    @PostMapping(value = "/rank")
    public void rankCreate(RankCreateDto rankCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/rank/{id}")
    public void rankUpdate(RankUpdateDto rankUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/rank/{id}")
    public void rankDelete(RankDeleteDto rankDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/rank")
    public void rankAll(RankAllDto rankAllDto, HttpServletRequest request) {

    }
}
