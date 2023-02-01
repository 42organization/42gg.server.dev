package io.pp.arcade.v1.admin.users.controller;

import io.pp.arcade.v1.admin.users.dto.UserDetailResponseAdminDto;
import io.pp.arcade.v1.admin.users.dto.UserSearchResponseAdminDto;
import io.pp.arcade.v1.admin.users.dto.UserUpdateRequesAdmintDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface UserAdminController {

    UserDetailResponseAdminDto userFindDetail(@PathVariable String targetUserId, @RequestBody Integer userId, HttpServletRequest request);
    UserSearchResponseAdminDto userAll(HttpServletRequest request, @RequestParam(value = "q", required = false) String keyword, @RequestParam(value = "page") Long page);
    void userUpdate(@RequestBody UserUpdateRequesAdmintDto userUpdateDto, HttpServletRequest request);
}
