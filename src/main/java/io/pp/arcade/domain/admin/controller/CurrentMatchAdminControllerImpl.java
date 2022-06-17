package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.CurrentMatchAllDto;
import io.pp.arcade.domain.admin.dto.create.CurrentMatchCreateDto;
import io.pp.arcade.domain.admin.dto.delete.CurrentMatchDeleteDto;
import io.pp.arcade.domain.admin.dto.update.CurrentMatchUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class CurrentMatchAdminControllerImpl implements CurrentMatchAdminController {
    @Override
    @PostMapping(value = "/currentMatch")
    public void currentMatchCreate(CurrentMatchCreateDto currentMatchCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/currentMatch/{id}")
    public void currentMatchUpdate(CurrentMatchUpdateDto currentMatchUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/currentMatch/{id}")
    public void currentMatchDelete(CurrentMatchDeleteDto currentMatchDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/currentMatch")
    public void currentMatchAll(CurrentMatchAllDto currentMatchAllDto, HttpServletRequest request) {

    }
}
