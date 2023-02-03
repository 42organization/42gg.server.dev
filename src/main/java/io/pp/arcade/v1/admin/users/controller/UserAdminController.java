package io.pp.arcade.v1.admin.users.controller;


import io.pp.arcade.v1.admin.users.dto.UserDetailResponseAdminDto;
import io.pp.arcade.v1.admin.users.dto.UserSearchResponseAdminDto;
import io.pp.arcade.v1.admin.users.dto.UserUpdateRequesAdmintDto;
import io.pp.arcade.v1.admin.dto.create.UserCreateRequestDto;
import io.pp.arcade.v1.admin.dto.update.UserUpdateRequestDto;
import io.pp.arcade.v1.admin.users.dto.UserSearchResultAdminResponseDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserAdminController {

    UserDetailResponseAdminDto userFindDetail(@PathVariable String targetUserId, @RequestBody Integer userId, HttpServletRequest request);
    UserSearchResponseAdminDto userAll(HttpServletRequest request, @RequestParam(value = "q", required = false) String keyword, @RequestParam(value = "page") Long page);
    void userDetailUpdate(@RequestBody UserUpdateRequesAdmintDto userUpdateDto, HttpServletRequest request);
    UserSearchResultAdminResponseDto userSearchResult(@RequestParam(value="q", required = false, defaultValue = "") String inquiringString/* HttpServletRequest request*/);
}
