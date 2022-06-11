package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDetailResponseDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserHistoricResponseDto;
import io.pp.arcade.domain.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
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
    public List<UserHistoricResponseDto> findUserHistorics(Integer userId) {
        List<PChangeDto> pChangeList = pChangeService.findPChangeByUserId(PChangeFindDto.builder()
                .userId(userId)
                .build());
        List<UserHistoricResponseDto> responseDto = new ArrayList<UserHistoricResponseDto>();
        for (PChangeDto dto : pChangeList){
            if (pChangeList.indexOf(dto) >= 10)
                break;
            responseDto.add(UserHistoricResponseDto.builder()
                    .gameId(dto.getGameId())
                    .userId(dto.getUserId())
                    .pppChange(dto.getPppChange())
                    .pppResult(dto.getPppResult())
                    .build());
        }
        return responseDto;
    }
}
