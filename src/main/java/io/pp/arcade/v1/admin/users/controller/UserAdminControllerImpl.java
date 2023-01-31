package io.pp.arcade.v1.admin.users.controller;

import io.pp.arcade.v1.admin.dto.create.UserCreateRequestDto;
import io.pp.arcade.v1.admin.dto.update.UserUpdateRequestDto;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.domain.user.dto.UserFindDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class UserAdminControllerImpl implements UserAdminController {
    private final UserService userService;

    @Override
    @GetMapping(value = "/users")
    public List<UserDto> userAll(Long page, HttpServletRequest request, String keyword) {
        Pageable pageable = PageRequest.of(page.intValue() - 1, 20);

        if (keyword == null) {
            List<UserDto> users = userService.findUserByAdmin(pageable);
            return users;
        }
        return null;
    }

    @Override
    @PutMapping(value = "/users/{intraid}/detail")
    public void userUpdate(UserUpdateRequestDto updateRequestDto, HttpServletRequest request) {
        userService.updateUserByAdmin(updateRequestDto);
    }

//    @Override
//    @DeleteMapping(value = "/users/{userId}")
//    public void userRoleChange(Integer userId, HttpServletRequest request) {
//        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
//        userService.toggleUserRoleType(user);
//    }
}
