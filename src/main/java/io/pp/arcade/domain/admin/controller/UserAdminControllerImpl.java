package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.UserCreateDto;
import io.pp.arcade.domain.admin.dto.update.UserUpdateDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.domain.user.dto.UserModifyProfileDto;
import lombok.AllArgsConstructor;
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
    @PostMapping(value = "/user")
    public void userCreate(UserCreateDto userCreateDto, HttpServletRequest request) {
        userService.createUserByAdmin(userCreateDto);
        throw new ResponseStatusException(HttpStatus.CREATED);
    }

    @Override
    @PutMapping(value = "/user/{id}")
    public void userUpdate(Integer id, UserUpdateDto userUpdateDto, HttpServletRequest request) {
        userService.modifyUserPpp(UserModifyPppDto.builder().userId(id).ppp(userUpdateDto.getPpp()).build());
        UserModifyProfileDto modifyProfileDto = UserModifyProfileDto.builder()
                .userId(id)
                .email(userUpdateDto.getEmail())
                .userImageUri(userUpdateDto.getUserImageUri())
                .racketType(userUpdateDto.getRacketType())
                .statusMessage(userUpdateDto.getStatusMessage())
                .build();
        userService.modifyUserProfile(modifyProfileDto);
    }

    @Override
    @GetMapping(value = "/user")
    public List<UserDto> userAll(Pageable pageable, HttpServletRequest request) {
        List<UserDto> users = userService.findUserByAdmin(pageable);
        return users;
    }
}
