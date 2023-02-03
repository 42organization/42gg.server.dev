package io.pp.arcade.v1.admin.users.controller;

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
    List<UserDto> userAll(@RequestParam("page") Long page, HttpServletRequest request, @RequestParam("q") String keyword);
    void userUpdate(@RequestBody UserUpdateRequestDto userUpdateDto, HttpServletRequest request);
    UserSearchResultAdminResponseDto userSearchResult(@RequestParam(value="q", required = false, defaultValue = "") String inquiringString/* HttpServletRequest request*/);
}
