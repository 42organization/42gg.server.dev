package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.UserAllDto;
import io.pp.arcade.domain.admin.dto.create.UserCreateDto;
import io.pp.arcade.domain.admin.dto.update.UserUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class UserAdminControllerImpl implements UserAdminController {
    @Override
    @PostMapping(value = "/user")
    public void userCreate(UserCreateDto userCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/user/{id}")
    public void userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/user")
    public void userAll(UserAllDto userAllDto, HttpServletRequest request) {

    }
}
