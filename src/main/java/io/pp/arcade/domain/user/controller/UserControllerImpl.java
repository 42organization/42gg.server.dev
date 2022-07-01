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
import io.pp.arcade.domain.rank.dto.RankFindDto;
import io.pp.arcade.domain.rank.dto.RankModifyStatusMessageDto;
import io.pp.arcade.domain.rank.dto.RankUserDto;
import io.pp.arcade.domain.rank.service.RankRedisService;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.*;
import io.pp.arcade.global.type.GameType;
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
    private final RankRedisService rankRedisService;

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
    @GetMapping(value = "/users/{targetUserId}/detail")
    public UserDetailResponseDto userFindDetail(String targetUserId, HttpServletRequest request) {
        UserDto curUser = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        UserDto targetUser = userService.findByIntraId(UserFindDto.builder().intraId(targetUserId).build());
        // 일단 게임타입은 SINGLE로 구현
        RankUserDto rankDto = rankRedisService.findRankById(RankFindDto.builder().intraId(targetUserId).gameType(GameType.SINGLE).build());
        UserRivalRecordDto rivalRecord = UserRivalRecordDto.builder().curUserWin(0).targetUserWin(0).build();
        if (!targetUserId.equals(curUser.getIntraId())) {
            rivalRecord = getRivalRecord(curUser, targetUser);
        }
        UserDetailResponseDto responseDto = UserDetailResponseDto.builder()
                .intraId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .ppp(targetUser.getPpp())
                .statusMessage(targetUser.getStatusMessage())
                .wins(rankDto.getWins())
                .losses(rankDto.getLosses())
                .winRate(rankDto.getWinRate())
                .rank(rankDto.getRank())
                .rivalRecord(rivalRecord)
                .build();
        return responseDto;
    }

    private UserRivalRecordDto getRivalRecord(UserDto curUser, UserDto targetUser) {
        List<PChangeDto> curUserPChanges = pChangeService.findPChangeByUserIdNotPage(PChangeFindDto.builder().userId(curUser.getIntraId()).build());
        List<PChangeDto> targetUserPChanges = pChangeService.findPChangeByUserIdNotPage(PChangeFindDto.builder().userId(targetUser.getIntraId()).build());
        Integer curUserWin = 0;
        Integer tarGetUserWin = 0;
        for (PChangeDto curUsers : curUserPChanges) {
            for (PChangeDto targetUsers : targetUserPChanges) {
                if (curUsers.getGame().getId().equals(targetUsers.getGame().getId())
                        && (curUsers.getPppChange() * targetUsers.getPppChange() < 0)) {
                    if (curUsers.getPppChange() > 0) {
                        curUserWin += 1;
                    } else if (curUsers.getPppChange() < 0) {
                        tarGetUserWin += 1;
                    }
                } else {
                    targetUserPChanges.remove(targetUsers);
                }
            }
        }
        UserRivalRecordDto dto = UserRivalRecordDto.builder().curUserWin(curUserWin).targetUserWin(tarGetUserWin).build();
        return dto;
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
                    .ppp(dto.getPppResult())
                    .date(dto.getGame().getTime())
                    .build());
        }
        UserHistoricResponseDto responseDto = UserHistoricResponseDto.builder()
                .historics(historicDtos).build();
        return responseDto;
    }

    @Override
    @PutMapping(value = "/users/detail")
    public void userModifyProfile(HttpServletRequest request, UserModifyProfileRequestDto requestDto) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        RankModifyStatusMessageDto modifyDto = RankModifyStatusMessageDto.builder().statusMessage(requestDto.getStatusMessage()).intraId(user.getIntraId()).build();
        rankRedisService.modifyRankStatusMessage(modifyDto);
        userService.modifyUserProfile(UserModifyProfileDto.builder()
                .userId(user.getId())
                .racketType(requestDto.getRacketType())
                .statusMessage(requestDto.getStatusMessage()).build());
    }

    @Override
    @GetMapping(value = "/users/searches")
    public UserSearchResultResponseDto userSearchResult(String inquiringString) {
        List<String> users = userService.findByPartsOfIntraId(UserSearchRequestDto.builder().intraId(inquiringString).build())
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
