package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping (value = "/pingpong")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final PChangeService pChangeService;

    @Override
    @GetMapping(value = "/users")
    public UserResponseDto findUser(Integer userId) {
        UserDto user = userService.findById(userId);
        return UserResponseDto.builder()
                .userId(user.getIntraId())
                .userImageUri(user.getImageUri())
                //.notiCount()
                .build(); // notiCount 추가 필요!
    }

    @Override
    @GetMapping(value = "/users/{targetUserId}/detail")
    public UserDetailResponseDto findDetailUser(Integer targetUserId, Integer currentUserId) {
        /* 상대 전적 비교 */
        UserDto curUser = userService.findById(currentUserId);

        UserDto targetUser = userService.findById(targetUserId);
        return UserDetailResponseDto.builder()
                .userId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .ppp(targetUser.getPpp())
                .statusMessage(targetUser.getStatusMessage())
                //.wins()
                //.losses()
                //.winRate()
                //.rank()
                .build();
    }

    @Override
    @GetMapping(value = "/users/{userId}/historics")
    public UserHistoricResponseDto findUserHistorics(Integer userId, Pageable pageable) {
        PChangePageDto pChangePage = pChangeService.findPChangeByUserId(PChangeFindDto.builder()
                .userId(userId)
                .build(), pageable);
        List<PChangeDto> pChangeList = pChangePage.getPChangeList();
        List<UserHistoricDto> historicDtos = new ArrayList<UserHistoricDto>();
        for (PChangeDto dto : pChangeList){
            historicDtos.add(UserHistoricDto.builder()
                    .gameId(dto.getGame().getId())
                    .userId(dto.getUser().getId())
                    .pppChange(dto.getPppChange())
                    .pppResult(dto.getPppResult())
                    .build());
        }
        UserHistoricResponseDto responseDto = UserHistoricResponseDto.builder()
                .historics(historicDtos).build();
        return responseDto;
    }
}
