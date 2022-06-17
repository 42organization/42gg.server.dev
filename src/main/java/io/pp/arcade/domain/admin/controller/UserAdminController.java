package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.UserAllDto;
import io.pp.arcade.domain.admin.dto.create.UserCreateDto;
import io.pp.arcade.domain.admin.dto.update.UserUpdateDto;
import io.pp.arcade.domain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserAdminController {
    void userCreate(UserCreateDto userCreateDto, HttpServletRequest request);
    void userUpdate(@PathVariable Integer id, UserUpdateDto userUpdateDto, HttpServletRequest request);
    List<UserDto> userAll(Pageable pageable, HttpServletRequest request);
}
