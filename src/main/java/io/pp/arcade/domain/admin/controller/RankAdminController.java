package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.RankAllDto;
import io.pp.arcade.domain.admin.dto.create.RankCreateDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface RankAdminController {
    void rankCreate(RankCreateDto rankCreateDto, HttpServletRequest request);
    void rankUpdate(RankUpdateDto rankUpdateDto, HttpServletRequest request);
    void rankDelete(RankDeleteDto rankDeleteDto, HttpServletRequest request);
    void rankAll(RankAllDto rankAllDto, HttpServletRequest request);
}
