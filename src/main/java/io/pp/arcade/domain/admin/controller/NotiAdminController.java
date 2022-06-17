package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.NotiAllDto;
import io.pp.arcade.domain.admin.dto.create.NotiCreateDto;
import io.pp.arcade.domain.admin.dto.update.NotiUpdateDto;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;

import javax.servlet.http.HttpServletRequest;

public interface NotiAdminController {
    void notiCreate(NotiCreateDto notiCreateDto, HttpServletRequest request);
    void notiUpdate(NotiUpdateDto notiUpdateDto, HttpServletRequest request);
    void notiDelete(NotiDeleteDto notiDeleteDto, HttpServletRequest request);
    void notiAll(NotiAllDto notiAllDto, HttpServletRequest request);
}
