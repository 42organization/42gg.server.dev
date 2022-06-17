package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.TeamAllDto;
import io.pp.arcade.domain.admin.dto.create.TeamCreateDto;
import io.pp.arcade.domain.admin.dto.delete.TeamDeleteDto;
import io.pp.arcade.domain.admin.dto.update.TeamUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface TeamAdminController {
    void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request);
    void teamUpdate(@PathVariable Integer id, TeamUpdateDto teamUpdateDto, HttpServletRequest request);
    void teamDelete(@PathVariable Integer id, HttpServletRequest request);
    void teamAll(Pageable pageable, HttpServletRequest request);
}
