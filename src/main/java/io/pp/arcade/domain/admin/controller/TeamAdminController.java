package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.TeamAllDto;
import io.pp.arcade.domain.admin.dto.create.TeamCreateDto;
import io.pp.arcade.domain.admin.dto.delete.TeamDeleteDto;
import io.pp.arcade.domain.admin.dto.update.TeamUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface TeamAdminController {
    void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request);
    void teamUpdate(TeamUpdateDto teamUpdateDto, HttpServletRequest request);
    void teamDelete(TeamDeleteDto teamDeleteDto, HttpServletRequest request);
    void teamAll(TeamAllDto teamAllDto, HttpServletRequest request);
}
