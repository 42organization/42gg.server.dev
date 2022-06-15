package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.TeamAllDto;
import io.pp.arcade.domain.admin.dto.create.TeamCreateDto;
import io.pp.arcade.domain.admin.dto.delete.TeamDeleteDto;
import io.pp.arcade.domain.admin.dto.update.TeamUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class TeamAdminControllerImpl implements TeamAdminController {
    @Override
    @PostMapping(value = "/team")
    public void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/team/{id}")
    public void teamUpdate(TeamUpdateDto teamUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/team/{id}")
    public void teamDelete(TeamDeleteDto teamDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/team")
    public void teamAll(TeamAllDto teamAllDto, HttpServletRequest request) {

    }
}
