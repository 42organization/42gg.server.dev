package io.pp.arcade.domain.slot.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.domain.noti.dto.NotiCanceledTypeDto;
import io.pp.arcade.domain.season.Season;
import io.pp.arcade.domain.season.SeasonService;
import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamAddUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.team.dto.TeamRemoveUserDto;
import io.pp.arcade.domain.slot.dto.SlotStatusResponseDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.redis.Key;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.NotiType;
import io.pp.arcade.global.type.SlotStatusType;
import io.pp.arcade.global.util.HeaderUtil;
import io.pp.arcade.global.util.NotiGenerater;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    @GetMapping(value = "/match/tables/{tableId}/{type}")
    public SlotStatusResponseDto slotStatusList(Integer tableId, GameType type, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        List<SlotStatusDto> slots;
        List<SlotGroupDto> slotGroups;
        SlotFindStatusDto findDto = SlotFindStatusDto.builder()
                .userId(user.getId())
                .type(type)
                .currentTime(LocalDateTime.now())
                .build();
        slots = slotService.findSlotsStatus(findDto);
        slotGroups = groupingSlots(slots);
        SlotStatusResponseDto responseDto = SlotStatusResponseDto.builder().slotGroups(slotGroups).build();
        return responseDto;
    }

    @Override
    @PostMapping(value = "/match/tables/{tableId}/{type}")
    public void slotAddUser(Integer tableId, GameType type, SlotAddUserRequestDto addReqDto, HttpServletRequest request) throws MessagingException {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        Integer userId = user.getId();
        //user가 매치를 이미 가지고 있는지 myTable에서 user 필터하기
        CurrentMatchDto matchDto = currentMatchService.findCurrentMatchByUserId(userId);
        if (matchDto != null) {
            throw new BusinessException("{invalid.request}");
        }

        if (redisTemplate.opsForValue().get(Key.PENALTY_USER + user.getIntraId()) != null) {
            throw new BusinessException("{invalid.request}");
        }

        SlotAddUserDto addDto = SlotAddUserDto.builder()
                .slotId(addReqDto.getSlotId())
                .type(type)
                .joinUserPpp(user.getPpp())
                .build();
        SlotDto slot = slotService.findSlotById(addDto.getSlotId());
        TeamAddUserDto teamAddUserDto = getTeamAddUserDto(slot, type, user);

        //유저가 슬롯에 입장하면 currentMatch에 등록된다.
        CurrentMatchAddDto matchAddDto = CurrentMatchAddDto.builder()
                .slot(slot)
                .userId(userId)
                .build();
        currentMatchService.addCurrentMatch(matchAddDto);
        slotService.addUserInSlot(addDto);
        teamService.addUserInTeam(teamAddUserDto);

        slot = slotService.findSlotById(slot.getId());

        //유저가 슬롯에 꽉 차면 currentMatch가 전부 바뀐다.
        modifyUsersCurrentMatchStatus(user, slot);
        notiGenerater.addMatchNotisBySlot(slot);
    }

    @Override
    @DeleteMapping(value = "/match")
    public void slotRemoveUser(HttpServletRequest request) throws MessagingException {
        // slotId , tableId 유효성 검사
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        // 유저 조회, 슬롯 조회, 팀 조회( 슬롯에 헤드 카운트 -, 팀에서 유저 퇴장 )
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUserId(user.getId());
        if (currentMatch == null) {
            throw new BusinessException("{invalid.request}");
        } else if (currentMatch.getMatchImminent()) {
            throw new BusinessException("{invalid.request}");
        } else if (currentMatch.getGame() != null) {
            throw new BusinessException("{invalid.request}");
        }
        SlotDto slot = currentMatch.getSlot();
        CurrentMatchRemoveDto currentMatchRemoveDto = CurrentMatchRemoveDto.builder()
                .userId(user.getId()).build();
        if (currentMatch.getIsMatched() == true) {
            falsifyIsMatchedForRemainders(currentMatch.getSlot());
        }
        currentMatchService.removeCurrentMatch(currentMatchRemoveDto);
        teamService.removeUserInTeam(getTeamRemoveUserDto(slot, user));
        slotService.removeUserInSlot(getSlotRemoveUserDto(slot, user));
        if (currentMatch.getIsMatched() == true) {
            notiGenerater.addCancelNotisBySlot(NotiCanceledTypeDto.builder().slotDto(slot).notiType(NotiType.CANCELEDBYMAN).build());
        }
    }

    private void falsifyIsMatchedForRemainders(SlotDto slot) {
        List<UserDto> users = new ArrayList<>();
        users.add(slot.getTeam1().getUser1());
        users.add(slot.getTeam1().getUser2());
        users.add(slot.getTeam2().getUser1());
        users.add(slot.getTeam2().getUser2());

        for (UserDto user : users) {
            if (user != null) {
                currentMatchService.modifyCurrentMatch(CurrentMatchModifyDto.builder()
                        .userId(user.getId())
                        .isMatched(false)
                        .matchImminent(false)
                        .build());
            }
        }
    }

    private List<SlotGroupDto> groupingSlots(List<SlotStatusDto> slots) {
        List<SlotGroupDto> slotGroups = new ArrayList<>();
        if (!slots.isEmpty()) {
            List<SlotStatusDto> oneGroup = new ArrayList<>();
            int groupTime = slots.get(0).getTime().getHour();

            for(SlotStatusDto slot: slots) {
                if (slot.getTime().getHour() == groupTime) {
                    oneGroup.add(SlotStatusDto.builder()
                            .slotId(slot.getSlotId())
                            .time(slot.getTime())
                            .headCount(slot.getHeadCount())
                            .status(slot.getStatus()).build());
                } else {
                    slotGroups.add(SlotGroupDto.builder()
                            .slots(oneGroup).build());
                    oneGroup = new ArrayList<>(); //다음 그루핑을 위한 그룹 생성
                    groupTime = slot.getTime().getHour(); //시간 갱신
                    oneGroup.add(SlotStatusDto.builder()
                            .slotId(slot.getSlotId())
                            .time(slot.getTime())
                            .headCount(slot.getHeadCount())
                            .status(slot.getStatus()).build());
                }
            }
            slotGroups.add(SlotGroupDto.builder()
                    .slots(oneGroup).build());
        }


        return slotGroups;
    }

    private void modifyUsersCurrentMatchStatus(UserDto user, SlotDto slot) {
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer maxSlotHeadCount = GameType.SINGLE.equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = slot.getHeadCount().equals(maxSlotHeadCount);
        Boolean isImminent = slot.getTime().isBefore(LocalDateTime.now().plusMinutes(5));
        CurrentMatchModifyDto matchModifyDto = CurrentMatchModifyDto.builder()
                .userId(user.getId())
                .isMatched(isMatched)
                .matchImminent(isImminent)
                .build();
        modifyCurrentMatch(team1.getUser1(), matchModifyDto);
        modifyCurrentMatch(team1.getUser2(), matchModifyDto);
        modifyCurrentMatch(team2.getUser1(), matchModifyDto);
        modifyCurrentMatch(team2.getUser2(), matchModifyDto);
    }

    private TeamAddUserDto getTeamAddUserDto(SlotDto slot, GameType gameType, UserDto user) {
        Integer teamId;
        GameType slotType = slot.getType();
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer headCount = slot.getHeadCount();
        SeasonDto season = seasonService.findCurrentSeason();
        Integer pppGap;
        if (season == null) {
            pppGap = 100;
        } else {
            pppGap = season.getPppGap();
        }
        Integer maxTeamHeadCount = GameType.SINGLE.equals(slotType) ? 1 : 2;

        SlotFilterDto slotFilterDto = SlotFilterDto.builder()
                .slotId(slot.getId())
                .slotTime(slot.getTime())
                .slotType(slot.getType())
                .gameType(gameType)
                .userPpp(user.getPpp())
                .gamePpp(slot.getGamePpp())
                .pppGap(pppGap)
                .headCount(slot.getHeadCount())
                .build();
        if (SlotStatusType.OPEN.equals(slotService.getStatus(slotFilterDto))) {
            if (team1.getHeadCount() < maxTeamHeadCount) {
                teamId = team1.getId();
            } else {
                teamId = team2.getId();
            }
        } else {
            throw
                    new BusinessException("{invalid.request}");
        }
        TeamAddUserDto teamAddUserDto = TeamAddUserDto.builder()
                .teamId(teamId)
                .userId(user.getId())
                .build();
        return teamAddUserDto;
    }

    private TeamRemoveUserDto getTeamRemoveUserDto(SlotDto slot, UserDto user) {
        TeamPosDto teamPos = teamService.getTeamPosNT(user, slot.getTeam1(), slot.getTeam2());
        TeamRemoveUserDto teamRemoveUserDto = TeamRemoveUserDto.builder()
                .userId(user.getId())
                .teamId(teamPos.getMyTeam().getId())
                .build();
        return teamRemoveUserDto;
    }

    private SlotRemoveUserDto getSlotRemoveUserDto(SlotDto slot, UserDto user) {
        SlotRemoveUserDto slotRemoveUserDto = SlotRemoveUserDto.builder()
                .slotId(slot.getId())
                .userId(user.getIntraId())
                .exitUserPpp(user.getPpp())
                .build();
        return slotRemoveUserDto;
    }

    private void modifyCurrentMatch(UserDto user, CurrentMatchModifyDto modifyDto) {
        if (user != null) {
            CurrentMatchModifyDto matchModifyDto = CurrentMatchModifyDto.builder()
                    .userId(user.getId())
                    .isMatched(modifyDto.getIsMatched())
                    .matchImminent(modifyDto.getMatchImminent())
                    .build();
            currentMatchService.modifyCurrentMatch(matchModifyDto);
        }
    }
}