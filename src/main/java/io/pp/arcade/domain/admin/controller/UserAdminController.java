package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.UserAllDto;
import io.pp.arcade.domain.admin.dto.create.UserCreateDto;
import io.pp.arcade.domain.admin.dto.update.UserUpdateDto;

import javax.servlet.http.HttpServletRequest;

public interface UserAdminController {
    void userCreate(UserCreateDto userCreateDto, HttpServletRequest request);
    void userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request);
    void userAll(UserAllDto userAllDto, HttpServletRequest request);
}
