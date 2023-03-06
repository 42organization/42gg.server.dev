package io.pp.arcade.v1.domain.slot.controller;

import io.pp.arcade.v1.admin.slot.SlotManagement;
import io.pp.arcade.v1.admin.slot.service.SlotAdminService;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.v1.domain.noti.dto.NotiCanceledTypeDto;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.slot.SlotService;

import io.pp.arcade.v1.domain.team.TeamService;
import io.pp.arcade.v1.domain.team.dto.TeamAddUserDto;
import io.pp.arcade.v1.domain.team.dto.TeamDto;
import io.pp.arcade.v1.domain.team.dto.TeamRemoveUserDto;
import io.pp.arcade.v1.domain.slot.dto.SlotStatusResponseDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.redis.Key;
import io.pp.arcade.v1.global.scheduler.SlotGenerator;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.NotiType;
import io.pp.arcade.v1.global.type.SlotStatusType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import io.pp.arcade.v1.global.util.NotiGenerater;
import io.pp.arcade.v1.domain.slot.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class SlotControllerImpl implements SlotController {
    private final SlotService slotService;
    private final TeamService teamService;
    private final CurrentMatchService currentMatchService;
    private final NotiGenerater notiGenerater;
    private final SeasonService seasonService;
    private final TokenService tokenService;
    private final RedisTemplate redisTemplate;
    private final SlotAdminService slotAdminService;

    @Override
    @GetMapping(value = "/match/tables/{tableId}/{mode}/{type}")
    public SlotStatusResponseDto slotStatusList(Integer tableId, Mode mode, GameType type, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        List<SlotStatusDto> slots;
        List<List<SlotStatusDto>> matchBoards;
        SlotFindStatusDto findDto = SlotFindStatusDto.builder()
                .userId(user.getId())
                .type(type)
                .currentTime(LocalDateTime.now())
                .userMode(mode)
                .build();
        slots = slotService.findSlotsStatus(findDto);
        matchBoards = groupingSlots(slots);
        SlotStatusResponseDto responseDto = SlotStatusResponseDto.builder().matchBoards(matchBoards).build();
        return responseDto;
    }

    @Override
    @PostMapping(value = "/match/tables/{tableId}/{type}")
    public synchronized void slotAddUser(Integer tableId, GameType type, SlotAddUserRequestDto addReqDto, HttpServletRequest request) throws MessagingException {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        doubleNotSupportedYet(type);
        SlotDto slot = slotService.findSlotById(addReqDto.getSlotId());

        checkIfUserHaveCurrentMatch(user);
        checkIfUserHavePenalty(user);
        checkIfModeMatches(addReqDto, slot);
        checkIfSlotAvailable(slot, type, user, addReqDto);

        //user가 들어갈 팀을 정한당
        TeamAddUserDto teamAddUserDto = getTeamAddUserDto(slot, user);

        //유저가 슬롯에 입장하면 currentMatch에 등록된다.
        CurrentMatchAddDto matchAddDto = CurrentMatchAddDto.builder()
                .slot(slot)
                .user(user)
                .build();
        currentMatchService.addCurrentMatch(matchAddDto);

        SlotAddUserDto addDto = SlotAddUserDto.builder()
                .slotId(addReqDto.getSlotId())
                .type(type)
                .joinUserPpp(user.getPpp())
                .mode(addReqDto.getMode())
                .build();
        slotService.addUserInSlot(addDto);
        teamService.addUserInTeam(teamAddUserDto);

        //유저가 슬롯에 꽉 차면 currentMatch가 전부 바뀐다.
        slot = slotService.findSlotById(slot.getId());
        modifyUsersCurrentMatchStatus(user, slot);
        notiGenerater.addMatchNotisBySlot(slot);
    }

    private void checkIfModeMatches(SlotAddUserRequestDto addReqDto, SlotDto slot) {
        if (addReqDto.getMode() == null) {
            throw new BusinessException("SC001");
        }
        if (slot.getMode() != Mode.BOTH && slot.getMode() != addReqDto.getMode()) {
            throw new BusinessException("SC001");
        }
    }

    private void doubleNotSupportedYet(GameType type) {
        if (GameType.DOUBLE.equals(type)) {
            throw new BusinessException("SC004");
        }
    }

    @Override
    @DeleteMapping(value = "/match/slots/{slotId}")
    public synchronized void slotRemoveUser(Integer slotId, HttpServletRequest request) throws MessagingException {
        // slotId , tableId 유효성 검사
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        // 유저 조회, 슬롯 조회, 팀 조회( 슬롯에 헤드 카운트 -, 팀에서 유저 퇴장 )
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUser(user);

        checkIfCurrentMatchExists(currentMatch);
        SlotDto slot = slotService.findSlotById(slotId);
        checkIfUserRemovable(currentMatch, slot);

        CurrentMatchRemoveDto currentMatchRemoveDto = CurrentMatchRemoveDto.builder()
                .user(user).build();
        currentMatchService.removeCurrentMatch(currentMatchRemoveDto);
        teamService.removeUserInTeam(TeamRemoveUserDto.builder()
                .slotId(slot.getId()).userId(user.getId()).build());
        slotService.removeUserInSlot(getSlotRemoveUserDto(slot, user));
//        slot = slotService.findSlotById(slot.getId());
        checkIsSlotMatched(user, currentMatch, slot);
    }
    private void checkIsSlotMatched(UserDto user, CurrentMatchDto currentMatch, SlotDto slot) throws MessagingException {
        if (currentMatch.getIsMatched() == true) {
            falsifyIsMatchedForRemainders(slot);
            redisTemplate.opsForValue().set(Key.PENALTY_USER + user.getIntraId(), "true", 60, TimeUnit.SECONDS);
            notiGenerater.addCancelNotisBySlot(NotiCanceledTypeDto.builder().slotDto(slot).notiType(NotiType.CANCELEDBYMAN).build());
        }
    }


    private void checkIfUserRemovable(CurrentMatchDto currentMatch, SlotDto slot) {
        if (currentMatch.getSlot().getId() != slot.getId()) {
            throw new BusinessException("E0001");
        }
        if (currentMatch.getMatchImminent() && slot.getHeadCount() == (slot.getType().equals(GameType.SINGLE) ? 2 : 4)) {
            throw new BusinessException("SD002");
        }
    }

    private void checkIfCurrentMatchExists(CurrentMatchDto currentMatch) {
        if (currentMatch == null || currentMatch.getGame() != null) {
            throw new BusinessException("SD001");
        }
    }

    private void falsifyIsMatchedForRemainders(SlotDto slot) {
        List<UserDto> users = new ArrayList<>();

        currentMatchService.modifyCurrentMatch(CurrentMatchModifyDto.builder()
                .slot(slot)
                .isMatched(false)
                .matchImminent(false)
                .build());
    }

    private List<List<SlotStatusDto>> groupingSlots(List<SlotStatusDto> slotStatusDtos) {
        List<List<SlotStatusDto>> slotGroups = new ArrayList<>();
        if (!slotStatusDtos.isEmpty()) {
            List<SlotStatusDto> oneGroup = new ArrayList<>();
            int groupTime = slotStatusDtos.get(0).getTime().getHour();

            for(SlotStatusDto slot: slotStatusDtos) {
                if (slot.getTime().getHour() == groupTime) {
                    oneGroup.add(slot);
                } else {
                    slotGroups.add(oneGroup);
                    oneGroup = new ArrayList<>(); //다음 그루핑을 위한 그룹 생성
                    groupTime = slot.getTime().getHour(); //시간 갱신
                    oneGroup.add(slot);
                }
            }
            slotGroups.add(oneGroup);
        }


        return slotGroups;
    }

    private void modifyUsersCurrentMatchStatus(UserDto user, SlotDto slot) {
        SlotManagement nowSlotPolicy = slotAdminService.getNowSlotPolicy();
        Integer maxSlotHeadCount = GameType.SINGLE.equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = (slot.getHeadCount()).equals(maxSlotHeadCount);
        Boolean isImminent = slot.getTime().isBefore(LocalDateTime.now().plusMinutes(nowSlotPolicy.getOpenMinute()));
        CurrentMatchModifyDto matchModifyDto = CurrentMatchModifyDto.builder()
                .slot(slot)
                .isMatched(isMatched)
                .matchImminent(isImminent)
                .isDel(false)
                .build();
        currentMatchService.modifyCurrentMatch(matchModifyDto);
    }

    private TeamAddUserDto getTeamAddUserDto(SlotDto slot, UserDto user) {
        Integer teamId = null;
        List<TeamDto> teams = teamService.findAllBySlotId(slot.getId());
        GameType slotType = slot.getType();
        Integer maxTeamHeadCount = GameType.SINGLE.equals(slotType) ? 1 : 2;

        for (TeamDto team : teams) {
            if (team.getHeadCount() < maxTeamHeadCount) {
                teamId = team.getId();
                break;
            }
        }
        TeamAddUserDto teamAddUserDto = TeamAddUserDto.builder()
                .teamId(teamId)
                .userId(user.getId())
                .build();
        return teamAddUserDto;
    }

    private void checkIfSlotAvailable(SlotDto slot, GameType gameType, UserDto user, SlotAddUserRequestDto requestDto) {
        Integer pppGap = getPppGapFromSeason();

        SlotFilterDto slotFilterDto = SlotFilterDto.builder()
                .slot(slot)
                .gameType(gameType)
                .userPpp(user.getPpp())
                .userMode(requestDto.getMode())
                .pppGap(pppGap)
                .build();
        if (SlotStatusType.CLOSE.equals(slotService.getStatus(slotFilterDto))) {
            throw new BusinessException("SC001");
        }
    }

    private Integer getPppGapFromSeason() {
        Integer pppGap;
        SeasonDto season = seasonService.findCurrentRankSeason();
        if (season == null) {
            pppGap = 100;
        } else {
            pppGap = season.getPppGap();
        }
        return pppGap;
    }

    private SlotRemoveUserDto getSlotRemoveUserDto(SlotDto slot, UserDto user) {
        SlotRemoveUserDto slotRemoveUserDto = SlotRemoveUserDto.builder()
                .slotId(slot.getId())
                .intraId(user.getIntraId())
                .exitUserPpp(user.getPpp())
                .build();
        return slotRemoveUserDto;
    }

    private void checkIfUserHavePenalty(UserDto user) {
        if (redisTemplate.opsForValue().get(Key.PENALTY_USER + user.getIntraId()) != null) {
            throw new BusinessException("SC003");
        }
        if (redisTemplate.opsForValue().get(Key.PENALTY_USER_ADMIN + user.getIntraId()) != null) {
            throw new BusinessException("SC005");
        }
    }

        private void checkIfUserHaveCurrentMatch(UserDto user) {
        CurrentMatchDto matchDto = currentMatchService.findCurrentMatchByUser(user);
        if (matchDto != null) {
            throw new BusinessException("SC002");
        }
    }
}
