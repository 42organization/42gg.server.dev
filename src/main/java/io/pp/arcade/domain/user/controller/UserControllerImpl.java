package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiCountDto;
import io.pp.arcade.domain.noti.dto.NotiFindDto;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.*;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final TokenService tokenService;
    /* *
     * [메인 페이지]
     * 유저 기본 정보 조회
     * */
    @Override
    @GetMapping(value = "/users")
    public UserResponseDto userFind(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        UserResponseDto responseDto = UserResponseDto.builder()
                .intraId(user.getIntraId())
                .userImageUri(user.getImageUri())
                .build();
        return responseDto;
    }

    /* *
     * [프로필 페이지]
     * 유저 프로필 정보 조회
     * */
    @Override
    @GetMapping(value = "/users/{userId}/detail")
    public UserDetailResponseDto userFindDetail(String targetUserId, HttpServletRequest request) {
        /* 상대 전적 비교 */
        UserDto curUser = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
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
    public UserHistoricResponseDto userFindHistorics(String userId, Pageable pageable) {
        PChangePageDto pChangePage = pChangeService.findPChangeByUserId(PChangeFindDto.builder()
                .userId(userId)
                .pageable(pageable)
                .build());
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
    public void userModifyProfile(String intraId, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        if (!(user.getIntraId().equals(intraId))) {
            throw new BusinessException("{invalid.request}");
        }
        userService.modifyUserProfile(UserModifyProfileDto.builder()
                .userId(user.getId())
                .userImageUri(user.getImageUri())
                .racketType(user.getRacketType())
                .statusMessage(user.getStatusMessage()).build());
    }

    @Override
    @GetMapping(value = "/users/searches")
    public UserSearchResultResponseDto userSearchResult(String inquiringString) {
        List<String> users = userService.findByPartsOfIntraId(UserSearchDto.builder().intraId(inquiringString).build())
                .stream().map(UserDto::getIntraId).collect(Collectors.toList());
        return UserSearchResultResponseDto.builder().users(users).build();
    }

    @Override
    @GetMapping(value = "/users/live")
    public UserLiveInfoResponseDto userLiveInfo(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByIntraId(user.getIntraId());
        GameDto currentMatchGame = currentMatch == null ? null : currentMatch.getGame();
        String event = currentMatch == null ? null : "match";
        if ("match".equals(event) && currentMatch.getGame() != null) {
            event = "game";
        }
        NotiCountDto notiCount = notiService.countAllNByUser(NotiFindDto.builder().user(user).build());
        UserLiveInfoResponseDto userLiveInfoResponse = UserLiveInfoResponseDto.builder()
                .notiCount(notiCount.getNotiCount())
                .event(event)
                .build();
        return userLiveInfoResponse;
    }
}
