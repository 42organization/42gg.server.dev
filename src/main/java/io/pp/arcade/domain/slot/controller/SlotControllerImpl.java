package io.pp.arcade.domain.slot.controller;

import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.*;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamAddUserDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamRemoveUserDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.slot.dto.SlotFindResponseDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class SlotControllerImpl implements SlotController {
    private final SlotService slotService;
    private final UserService userService;
    private final TeamService teamService;

    @Override
    @GetMapping(value = "/match/tables/{tableId}")
    public SlotFindResponseDto slotStatusList(Integer tableId, String type, Integer userId) {
        List<SlotStatusDto> slots;
        SlotFindStatusDto findDto = SlotFindStatusDto.builder()
                .userId(userId)
                .type(type)
                .currentTime(LocalDateTime.now())
                .build();
        slots = slotService.findSlotsStatus(findDto);
        SlotFindResponseDto responseDto = SlotFindResponseDto.builder().matchBoards(slots).build();
        return responseDto;
    }

    @Override
    @PostMapping(value = "/match/tables/{tableId}")
    public void slotAddUser(Integer tableId, SlotAddUserRequestDto addReqDto, Integer userId) {
        UserDto user = userService.findById(userId);
        //user가 매치를 이미 가지고 있는지 myTable에서 user 필터하기
        SlotAddUserDto addDto = SlotAddUserDto.builder()
                .slotId(addReqDto.getSlotId())
                .type(addReqDto.getType())
                .joinUserPpp(user.getPpp())
                .build();
        SlotDto slot = slotService.findSlotById(addDto.getSlotId());

        //type 확인하고 headCount 확인하고 어느팀에 보낼지 정해야 함.
        Integer teamId;
        String slotType = slot.getType();
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer headCount = slot.getHeadCount();
        Integer maxHeadCount = "single".equals(slotType) ? 1 : 2;
        //
        SlotFilterDto slotFilterDto = SlotFilterDto.builder()
                .slotTime(slot.getTime())
                .slotType(slot.getType())
                .requestType(addReqDto.getType())
                .userPpp(user.getPpp())
                .gamePpp(slot.getGamePpp())
                .headCount(slot.getHeadCount())
                .build();
        if (slotService.getStatus(slotFilterDto).equals("open")) {
            if (team1.getHeadCount() < maxHeadCount) {
                teamId = team1.getId();
            } else {
                teamId = team2.getId();
            }
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        TeamAddUserDto teamAddUserDto = TeamAddUserDto.builder()
                .teamId(teamId)
                .userId(userId)
                .build();
        slotService.addUserInSlot(addDto);
        teamService.addUserInTeam(teamAddUserDto);
    }

    @Override
    @DeleteMapping(value = "/match/tables/{tableId}")
    public void slotRemoveUser(Integer tableId, Integer slotId, Integer pUserId) {
        // slotId , tableId 유효성 검사
        UserDto user = userService.findById(pUserId);
        // 유저 조회, 슬롯 조회, 팀 조회( 슬롯에 헤드 카운트 -, 팀에서 유저 퇴장 )
        SlotDto slot = slotService.findSlotById(slotId);
        TeamDto team1 = slot.getTeam1();
        TeamDto team2 = slot.getTeam2();
        Integer teamId;
        Integer userId = user.getId();
        if (userId.equals(team1.getUser1().getId()) || userId.equals(team1.getUser2().getId())) {
            teamId = team1.getId();
        } else if(userId.equals(team2.getUser1().getId()) || userId.equals(team2.getUser2().getId())){
            teamId = team2.getId();
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        TeamRemoveUserDto teamRemoveUserDto = TeamRemoveUserDto.builder()
                .userId(user.getId())
                .teamId(teamId)
                .build();
        SlotRemoveUserDto slotRemoveUserDto = SlotRemoveUserDto.builder()
                .slotId(slot.getId())
                .exitUserPpp(user.getPpp())
                .build();
        teamService.removeUserInTeam(teamRemoveUserDto);
        slotService.removeUserInSlot(slotRemoveUserDto);
    }
}
