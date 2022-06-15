package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.SeasonAllDto;
import io.pp.arcade.domain.admin.dto.create.SeasonCreateDto;
import io.pp.arcade.domain.admin.dto.update.SeasonUpdateDto;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;

import javax.servlet.http.HttpServletRequest;

public interface SeasonAdminController {
    void seasonCreate(SeasonCreateDto seasonCreateDto, HttpServletRequest request);
    void seasonUpdate(SeasonUpdateDto seasonUpdateDto, HttpServletRequest request);
    void seasonDelete(SeasonDeleteDto seasonDeleteDto, HttpServletRequest request);
    void seasonAll(SeasonAllDto seasonAllDto, HttpServletRequest request);
}
