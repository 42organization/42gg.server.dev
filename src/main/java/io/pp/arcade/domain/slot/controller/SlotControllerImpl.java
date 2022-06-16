package io.pp.arcade.domain.slot.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiAddDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamAddUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.team.dto.TeamRemoveUserDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.slot.dto.SlotStatusResponseDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class SlotControllerImpl implements SlotController {
    private final SlotService slotService;
    private final UserService userService;
    private final TeamService teamService;
    private final NotiService notiService;
    private final CurrentMatchService currentMatchService;

    @Override
    @GetMapping(value = "/match/tables/{tableId}")
    public SlotStatusResponseDto slotStatusList(Integer tableId, String type, Integer userId) {
        List<SlotStatusDto> slots;
        List<SlotGroupDto> slotGroups;
        SlotFindStatusDto findDto = SlotFindStatusDto.builder()
                .userId(userId)
                .type(type)
                .currentTime(LocalDateTime.now())
                .build();
        slots = slotService.findSlotsStatus(findDto);
        slotGroups = groupingSlots(slots);
        SlotStatusResponseDto responseDto = SlotStatusResponseDto.builder().slotGroups(slotGroups).build();
        return responseDto;
    }

    private List<SlotGroupDto> groupingSlots(List<SlotStatusDto> slots) {
        if (slots.size() == 0) {
            throw new IllegalArgumentException("?");
        }
        List<SlotGroupDto> slotGroups = new ArrayList<>();
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
            }
        }
        slotGroups.add(SlotGroupDto.builder()
                .slots(oneGroup).build());

        return slotGroups;
    }

    @Override
    @PostMapping(value = "/match/tables/{tableId}")
    public void slotAddUser(Integer tableId, SlotAddUserRequestDto addReqDto, Integer userId) throws MessagingException {
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        //user가 매치를 이미 가지고 있는지 myTable에서 user 필터하기
        CurrentMatchDto matchDto = currentMatchService.findCurrentMatchByUserId(userId);
        if (matchDto != null) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        SlotAddUserDto addDto = SlotAddUserDto.builder()
                .slotId(addReqDto.getSlotId())
                .type(addReqDto.getType())
                .joinUserPpp(user.getPpp())
                .build();
        SlotDto slot = slotService.findSlotById(addDto.getSlotId());
        TeamAddUserDto teamAddUserDto = getTeamAddUserDto(slot, addReqDto.getType(), user);

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
        addMatchNotisBySlot(slot);
    }

    @Override
    @DeleteMapping(value = "/match/tables/{tableId}")
    public void slotRemoveUser(Integer tableId, Integer slotId, Integer pUserId) throws MessagingException {
        // slotId , tableId 유효성 검사
        UserDto user = userService.findById(UserFindDto.builder().userId(pUserId).build());
        // 유저 조회, 슬롯 조회, 팀 조회( 슬롯에 헤드 카운트 -, 팀에서 유저 퇴장 )
        SlotDto slot = slotService.findSlotById(slotId);

        currentMatchService.removeCurrentMatch(user.getId());
        teamService.removeUserInTeam(getTeamRemoveUserDto(slot, user));
        slotService.removeUserInSlot(getSlotRemoveUserDto(slot, user));
        addCancelNotisBySlot(slot);
    }

    private void modifyUsersCurrentMatchStatus(UserDto user, SlotDto slot) {
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer maxSlotHeadCount = "single".equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = slot.getHeadCount().equals(maxSlotHeadCount) ? true : false;
        Boolean isImminent = slot.getTime().isBefore(LocalDateTime.now().plusMinutes(5)) ? true : false;
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

    private void addMatchNotisBySlot(SlotDto slot) throws MessagingException {
        Integer maxSlotHeadCount = "single".equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = slot.getHeadCount().equals(maxSlotHeadCount) ? true : false;
        Boolean isImminent = slot.getTime().isBefore(LocalDateTime.now().plusMinutes(5)) ? true : false;
        if (isImminent == true) {
            addNoti(slot.getTeam1().getUser1(), slot, "imminent");
            addNoti(slot.getTeam1().getUser2(), slot, "imminent");
            addNoti(slot.getTeam2().getUser1(), slot, "imminent");
            addNoti(slot.getTeam2().getUser2(), slot, "imminent");
        } else if (isMatched == true) {
            addNoti(slot.getTeam1().getUser1(), slot, "matched");
            addNoti(slot.getTeam1().getUser2(), slot, "matched");
            addNoti(slot.getTeam2().getUser1(), slot, "matched");
            addNoti(slot.getTeam2().getUser2(), slot, "matched");
        }
    }

    private void addCancelNotisBySlot(SlotDto slot) throws MessagingException {
        addNoti(slot.getTeam1().getUser1(), slot, "canceled");
        addNoti(slot.getTeam1().getUser2(), slot, "canceled");
        addNoti(slot.getTeam2().getUser1(), slot, "canceled");
        addNoti(slot.getTeam2().getUser2(), slot, "canceled");
    }

    private void addNoti(UserDto user, SlotDto slot, String type) throws MessagingException {
        if (user != null) {
            NotiAddDto notiAddDto = NotiAddDto.builder()
                    .user(user)
                    .slot(slot)
                    .notiType(type)
                    .build();
            notiService.addNoti(notiAddDto);
        }
    }

    private TeamAddUserDto getTeamAddUserDto(SlotDto slot, String reqType, UserDto user) {
        Integer teamId;
        String slotType = slot.getType();
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer headCount = slot.getHeadCount();
        Integer maxTeamHeadCount = "single".equals(slotType) ? 1 : 2;

        SlotFilterDto slotFilterDto = SlotFilterDto.builder()
                .slotId(slot.getId())
                .slotTime(slot.getTime())
                .slotType(slot.getType())
                .requestType(reqType)
                .userPpp(user.getPpp())
                .gamePpp(slot.getGamePpp())
                .headCount(slot.getHeadCount())
                .build();
        if (slotService.getStatus(slotFilterDto).equals("open")) {
            if (team1.getHeadCount() < maxTeamHeadCount) {
                teamId = team1.getId();
            } else {
                teamId = team2.getId();
            }
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
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
