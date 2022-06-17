package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.PChangeAllDto;
import io.pp.arcade.domain.admin.dto.create.PChangeCreateDto;
import io.pp.arcade.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.domain.admin.dto.update.PChangeUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface PChangeAdminController {
    void pChangeCreate(PChangeCreateDto pChangeCreateDto, HttpServletRequest request);
    void pChangeUpdate(PChangeUpdateDto pChangeUpdateDto, HttpServletRequest request);
    void pChangeDelete(PChangeDeleteDto pChangeDeleteDto, HttpServletRequest request);
    void pChangeAll(PChangeAllDto pChangeAllDto, HttpServletRequest request);
}
