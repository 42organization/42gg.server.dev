package io.pp.arcade.v1.domain.user.controller;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.noti.NotiService;
import io.pp.arcade.v1.domain.noti.dto.NotiCountDto;
import io.pp.arcade.v1.domain.noti.dto.NotiFindDto;
import io.pp.arcade.v1.domain.pchange.PChangeService;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.rank.dto.RankFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankModifyStatusMessageDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.*;
import io.pp.arcade.v1.global.scheduler.CurrentMatchUpdater;
import io.pp.arcade.v1.global.scheduler.GameGenerator;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.ExpLevelCalculator;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
    private final RankService rankService;
    private final SeasonService seasonService;
    private final GameGenerator gameGenerator;
    private final CurrentMatchUpdater currentMatchUpdater;
    /*
     * [메인 페이지]
     * - 유저 정보 조회
     * */
    @Override
    @GetMapping(value = "/users")
    public UserResponseDto userFind(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        SeasonDto season = seasonService.findCurrentSeason();
        UserResponseDto responseDto = UserResponseDto.builder()
                .intraId(user.getIntraId())
                .userImageUri(user.getImageUri())
                .seasonMode(season.getSeasonMode().getCode())
                .isAdmin(user.getRoleType() == RoleType.ADMIN)
                .build();
        return responseDto;
    }

    /*
     * [프로필 페이지]
     * - 유저 프로필 조회
     * */
    @Override
    @GetMapping(value = "/users/{targetUserId}/detail")
    public UserDetailResponseDto userFindDetail(String targetUserId, HttpServletRequest request) {
        UserDto curUser = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        UserDto targetUser = userService.findByIntraId(UserFindDto.builder().intraId(targetUserId).build());
        Integer currentExp = ExpLevelCalculator.getCurrentLevelMyExp(targetUser.getTotalExp());
        Integer maxExp = ExpLevelCalculator.getLevelMaxExp(ExpLevelCalculator.getLevel(targetUser.getTotalExp()));
        Integer expRate = (currentExp / maxExp) * 100;
        // 일단 게임타입은 SINGLE로 구현
        UserRivalRecordDto rivalRecord = UserRivalRecordDto.builder().curUserWin(0).targetUserWin(0).build();
        if (!targetUserId.equals(curUser.getIntraId())) {
            rivalRecord = getRivalRecord(curUser, targetUser);
        }
        UserDetailResponseDto responseDto = UserDetailResponseDto.builder()
                .intraId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .statusMessage(targetUser.getStatusMessage())
                .level(ExpLevelCalculator.getLevel(targetUser.getTotalExp()))
                .currentExp(currentExp)
                .maxExp(maxExp)
                .rivalRecord(rivalRecord)
                .build();
        return responseDto;
    }

    /*
     * [랭크 프로필 페이지]
     * - 시즌별 유저 랭크 프로필 조회
     * */
    @Override
    @GetMapping(value = "/users/{targetUserId}/rank")
    public UserRankResponseDto userFindRank(String targetUserId, Integer season, HttpServletRequest request) {
        UserDto curUser = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        UserDto targetUser = userService.findByIntraId(UserFindDto.builder().intraId(targetUserId).build());
        RankUserDto rankDto;
        // 일단 게임타입은 SINGLE로 구현

        if (season == 0) {
            season = seasonService.findCurrentSeason().getId();
        }

        if (season.equals(seasonService.findCurrentSeason().getId()))  {
            rankDto = rankRedisService.findRankById(RankFindDto.builder().intraId(targetUserId).gameType(GameType.SINGLE).build());
        } else {
            rankDto = getRankUserDtoFromSeasonAndTargetUser(season, targetUser);
        }
        UserRankResponseDto responseDto = UserRankResponseDto.builder()
                .rank(rankDto.getRank())
                .ppp(rankDto.getPpp())
                .wins(rankDto.getWins())
                .losses(rankDto.getLosses())
                .winRate(rankDto.getWinRate())
                .build();
        return responseDto;
    }

    private RankUserDto getRankUserDtoFromSeasonAndTargetUser(Integer season, UserDto targetUser) {
        RankUserDto rankDto;
        SeasonDto seasonDto = seasonService.findSeasonById(season);
        if (seasonDto != null) {
            RankDto temp = rankService.findBySeasonIdAndUserId(seasonDto.getId(), targetUser.getId());
            if (temp != null) {
                rankDto = RankUserDto.builder()
                        .intraId(targetUser.getIntraId())
                        .rank(temp.getRanking())
                        .wins(temp.getWins())
                        .losses(temp.getLosses())
                        .winRate((temp.getWins() + temp.getLosses()) == 0 ? 0 : ((double)temp.getWins() / (double)(temp.getLosses() + temp.getWins()) * 100))
                        .ppp(temp.getPpp())
                        .statusMessage(targetUser.getStatusMessage())
                        .build();
            } else {
                rankDto = null;
            }
        } else {
            rankDto = null;
        }
        return rankDto;
    }

    /*
     * [프로필 페이지]
     * - 유저 최근 전적 경향 조회
     * */
    @Override
    @GetMapping(value = "/users/{userId}/historics")
    public UserHistoricResponseDto userFindHistorics(String userId, Integer season, Pageable pageable) {
        UserDto user = userService.findByIntraId(UserFindDto.builder().intraId(userId).build());
        PChangePageDto pChangePage = pChangeService.findRankPChangeByUserId(PChangeFindDto.builder()
                .user(user)
                .season(season == 0 ? seasonService.findLatestRankSeason().getId() : season)
                .count(10)
                .build());
        List<PChangeDto> pChangeList = pChangePage.getPChangeList();
        List<UserHistoricDto> historicDtos = new ArrayList<UserHistoricDto>();
        for (PChangeDto dto : pChangeList) {
            historicDtos.add(UserHistoricDto.builder()
                    .ppp(dto.getPppResult())
                    .date(dto.getGame().getSlot().getTime())
                    .build());
        }
        UserHistoricResponseDto responseDto = UserHistoricResponseDto.builder()
                .historics(historicDtos).build();
        return responseDto;
    }

    /*
     * [프로필 페이지]
     * - 유저 프로필 수정
     */
    @Override
    @PutMapping(value = "/users/detail")
    public void userModifyProfile(UserModifyProfileRequestDto requestDto, HttpServletRequest request) {
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
    public UserSearchResultResponseDto userSearchResult(String inquiringString, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        List<String> users = userService.findByPartsOfIntraId(UserSearchRequestDto.builder().intraId(inquiringString).build())
                .stream().map(UserDto::getIntraId).collect(Collectors.toList());
        return UserSearchResultResponseDto.builder().users(users).build();
    }

    @Override
    @GetMapping(value = "/users/live")
    public UserLiveInfoResponseDto userLiveInfo(HttpServletRequest request) throws MessagingException {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByIntraId(user.getIntraId());
        GameDto currentMatchGame = currentMatch == null ? null : currentMatch.getGame();
        SeasonDto season = seasonService.findCurrentSeason();
        String event = currentMatch == null ? null : "match";
        doubleCheckForSchedulers(currentMatch);
        if ("match".equals(event) && currentMatch.getGame() != null) {
            event = "game";
        }
        NotiCountDto notiCount = notiService.countAllNByUser(NotiFindDto.builder().user(user).build());
        UserLiveInfoResponseDto userLiveInfoResponse = UserLiveInfoResponseDto.builder()
                .notiCount(notiCount.getNotiCount())
                .currentMatchMode(currentMatch == null ? null : currentMatch.getSlot().getMode().getCode()) // is it right to find mode in slot?
                .seasonMode(season.getSeasonMode().getCode())
                .event(event)
                .build();
        return userLiveInfoResponse;
    }

    private void doubleCheckForSchedulers(CurrentMatchDto currentMatch) throws MessagingException {
        if (schedulersNoProblem(currentMatch)) {
            return ;
        }
        if (currentMatch.getMatchImminent() == false) {
            checkForCurrentMatchUpdater(currentMatch);
        } else {
            checkForGameGenerator(currentMatch);
        }
    }

    private boolean schedulersNoProblem(CurrentMatchDto currentMatch) {
        if (currentMatch == null) {
            return true;
        }
        if (currentMatch.getIsMatched() == false) {
            return true;
        }
        return false;
    }

    private void checkForGameGenerator(CurrentMatchDto currentMatch) throws MessagingException {
        if (currentMatch.getGame() == null && LocalDateTime.now().isAfter(currentMatch.getSlot().getTime())) {
            gameGenerator.gameGenerator(currentMatch.getSlot().getTime());
        }
    }

    private void checkForCurrentMatchUpdater(CurrentMatchDto currentMatch) throws MessagingException {
        if (currentMatch.getGame() == null && LocalDateTime.now().isAfter(currentMatch.getSlot().getTime().minusMinutes(5))) {
            currentMatchUpdater.updateIsImminent(currentMatch.getSlot().getTime());
        }
    }

    private UserRivalRecordDto getRivalRecord(UserDto curUser, UserDto targetUser) {
        List<PChangeDto> curUserPChanges = pChangeService.findPChangeByUserIdNotPage(PChangeFindDto.builder().user(curUser).build());
        List<PChangeDto> targetUserPChanges = pChangeService.findPChangeByUserIdNotPage(PChangeFindDto.builder().user(targetUser).build());
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
                }
            }
        }
        UserRivalRecordDto dto = UserRivalRecordDto.builder().curUserWin(curUserWin).targetUserWin(tarGetUserWin).build();
        return dto;
    }
}
