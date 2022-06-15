package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.SeasonAllDto;
import io.pp.arcade.domain.admin.dto.create.SeasonCreateDto;
import io.pp.arcade.domain.admin.dto.update.SeasonUpdateDto;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class SeasonAdminControllerImpl implements SeasonAdminController {
    @Override
    @PostMapping(value = "/season")
    public void seasonCreate(SeasonCreateDto seasonCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/season/{id}")
    public void seasonUpdate(SeasonUpdateDto seasonUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/season/{id}")
    public void seasonDelete(SeasonDeleteDto seasonDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/season")
    public void seasonAll(SeasonAllDto seasonAllDto, HttpServletRequest request) {

    }
}
