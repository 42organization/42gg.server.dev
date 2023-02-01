package io.pp.arcade.v1.admin.users.controller;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.admin.users.service.UserAdminService;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class UserAdminControllerImpl implements UserAdminController {
    private final UserAdminService userAdminService;
    private final TokenService tokenService;

    @Override
    @GetMapping(value = "/users")
    public UserSearchResponseAdminDto userAll(HttpServletRequest request, String keyword, Long page) {
        Pageable pageable = PageRequest.of(page.intValue() - 1, 20);

        if (keyword == null) {
            UserSearchResponseAdminDto users = userAdminService.findUserByAdmin(pageable);
            return users;
        }
        UserSearchRequestAdminDto userSearchDto = UserSearchRequestAdminDto.builder()
                                                    .intraId(keyword)
                                                    .build();
        UserSearchResponseAdminDto users = userAdminService.findByPartsOfIntraId(userSearchDto, pageable);
        return users;
    }

    @Override
    @GetMapping(value = "/users/{intraId}/detail")
    public UserDetailResponseAdminDto userFindDetail(String intraId, Integer userId, HttpServletRequest request) {
        UserAdminDto targetUser = userAdminService.findByIntraId(UserFindAdminDto.builder().intraId(intraId).userId(userId).build());

        UserDetailResponseAdminDto responseDto = UserDetailResponseAdminDto.builder()
                .intraId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .statusMessage(targetUser.getStatusMessage())
                .build();
        return responseDto;
    }
    @Override
    @PutMapping(value = "/users/{intraid}/detail")
    public void userUpdate(UserUpdateRequesAdmintDto updateRequestDto, HttpServletRequest request) {
        userAdminService.updateUserByAdmin(updateRequestDto);
    }
}
