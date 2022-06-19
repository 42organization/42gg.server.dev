package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiCountDto;
import io.pp.arcade.domain.noti.dto.NotiFindDto;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final PChangeService pChangeService;
    private final NotiService notiService;
    private final CurrentMatchService currentMatchService;
    /* *
     * [메인 페이지]
     * 유저 기본 정보 조회
     * */
    @Override
    @GetMapping(value = "/users")
    public UserResponseDto userFind(Integer userId) {
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        UserResponseDto responseDto = UserResponseDto.builder()
                .intraId(user.getIntraId())
                .userImageUri(user.getImageUri())
                .build();// notiCount 추가 필요!
        return responseDto;
    }

    /* *
     * [프로필 페이지]
     * 유저 프로필 정보 조회
     * */
    @Override
    @GetMapping(value = "/users/{targetUserId}/detail")
    public UserDetailResponseDto userFindDetail(String targetUserId, Integer currentUserId) {
        /* 상대 전적 비교 */
        UserDto curUser = userService.findById(UserFindDto.builder().userId(currentUserId).build());
        UserDto targetUser = userService.findByIntraId(UserFindDto.builder().intraId(targetUserId).build());

        UserDetailResponseDto responseDto = UserDetailResponseDto.builder()
                .userId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .ppp(targetUser.getPpp())
                .statusMessage(targetUser.getStatusMessage())
                .wins(1)        // 추
                .losses(0)      // 후
                .winRate(100.0) // 구
                .rank(1)        // 현
                .build();
        return responseDto;
    }

    /* *
     * [프로필 페이지]
     * 유저 최근 전적 경향 조회
     * */
    @Override
    @GetMapping(value = "/users/{userId}/historics")
    public UserHistoricResponseDto userFindHistorics(String intraId, Pageable pageable) {
        PChangePageDto pChangePage = pChangeService.findPChangeByUserId(PChangeFindDto.builder()
                .intraId(intraId)
                .build(), pageable);
        List<PChangeDto> pChangeList = pChangePage.getPChangeList();
        List<UserHistoricDto> historicDtos = new ArrayList<UserHistoricDto>();
        for (PChangeDto dto : pChangeList) {
            historicDtos.add(UserHistoricDto.builder()
                    .gameId(dto.getGame().getId())
                    .userId(dto.getUser().getId())
                    .pppChange(dto.getPppChange())
                    .pppResult(dto.getPppResult())
                    .time(dto.getGame().getTime())
                    .build());
        }
        UserHistoricResponseDto responseDto = UserHistoricResponseDto.builder()
                .historics(historicDtos).build();
        return responseDto;
    }

    @Override
    @PutMapping(value = "/users/{intraId}/detail")
    public void userModifyProfile(UserModifyProfileRequestDto modifyRequestDto, String intraId) { //modifyPrfileRequestDto
        UserDto user = userService.findByIntraId(UserFindDto.builder()
                .intraId(intraId)
                .build());
        if (!(user.getIntraId().equals(intraId))) {
            throw new IllegalArgumentException("?!");
        }
        UserModifyProfileDto modifyDto = UserModifyProfileDto.builder()
                .userId(user.getId())
                .racketType(modifyRequestDto.getRacketType())
                .statusMessage(modifyRequestDto.getStatusMessage())
                .build();
        userService.modifyUserProfile(modifyDto);
    }

    @GetMapping(value = "/users/searches")
    public UserSearchResultResponseDto userSearchResult(String inquiringString) {
        List<String> users = userService.findByPartsOfIntraId(UserSearchDto.builder().intraId(inquiringString).build())
                .stream().map(UserDto::getIntraId).collect(Collectors.toList());
        return UserSearchResultResponseDto.builder().users(users).build();
    }

    @GetMapping(value = "/users/{intraId}/live")
    public UserLiveInfoResponseDto userLiveInfo(String intraId, Integer userId) {
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUserId(userId);
        if (!userId.equals(currentMatch.getUserId())) {
            throw new IllegalArgumentException("?");
        }
        UserDto user = userService.findByIntraId(UserFindDto.builder().intraId(intraId).build());
        NotiCountDto notiCount = notiService.countAllNByUser(NotiFindDto.builder().user(user).build());
        UserLiveInfoResponseDto userLiveInfoResponse = UserLiveInfoResponseDto.builder()
                .notiCount(notiCount.getNotiCount()) //
                .gameId(currentMatch.getGame() != null ? currentMatch.getGame().getId() : null)
                .build();
        return userLiveInfoResponse;
    }
}
