package io.gg.arcade.domain.slot.controller;

import io.gg.arcade.domain.slot.dto.SlotDto;
import io.gg.arcade.domain.slot.dto.SlotFindDto;
import io.gg.arcade.domain.slot.dto.SlotModifyRequestDto;
import io.gg.arcade.domain.slot.dto.SlotResponseDto;
import io.gg.arcade.domain.slot.service.SlotService;
import io.gg.arcade.domain.team.dto.TeamAddUserRequestDto;
import io.gg.arcade.domain.team.dto.TeamListRequestDto;
import io.gg.arcade.domain.team.dto.TeamListResponseDto;
import io.gg.arcade.domain.team.service.TeamService;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class SlotControllerImpl implements SlotController {
    private final SlotService slotService;
    private final TeamService teamService;
    private final UserService userService;

    @Override
    @GetMapping(value = "/pingpong/match/tables/{tableId}")
    public List<SlotResponseDto> findSlots(Integer tableId, String type, Integer userId) {
        UserDto user = userService.findById(userId);
        LocalDateTime now = LocalDateTime.now().minusDays(1);

        SlotFindDto slotFindDto = SlotFindDto.builder()
                .localDateTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() , 23, 59, 59))
                .currentUserPpp(user.getPpp())
                .inquiringType(type)
                .userId(user.getId())
                .build();
        return slotService.filterSlots(slotFindDto);
    }

    @Override
    @PostMapping(value = "/pingpong/match/tables/{tableId}")
    public void addUserInSlot(Integer tableId, SlotModifyRequestDto slotRequestDto, Integer userId) {
        SlotDto slot = slotService.findSlotById(slotRequestDto.getSlotId());
        UserDto user = userService.findById(userId);
        TeamListResponseDto teams = teamService.findTeamUserCountByTeamList(
                TeamListRequestDto.builder()
                .team1Id(slot.getTeam1Id())
                .team2Id(slot.getTeam2Id())
                .build()
        );

        Integer gamePpp;
        if (slot.getGamePpp() == null) {
            gamePpp = user.getPpp();
        } else if (Math.abs(slot.getGamePpp() - user.getPpp()) < 200) {
            gamePpp = slot.getGamePpp();
        } else {
            throw new RuntimeException();
        }

        SlotModifyRequestDto addDto = SlotModifyRequestDto.builder()
                .slotId(slotRequestDto.getSlotId())
                .gamePpp(gamePpp)
                .build();

        // 복식 추가
        String teamId;
        Integer maxHeadCount = 1;
        if (slotRequestDto.getType() != null && slotRequestDto.getType().equals("double")) {
            maxHeadCount = 2;
        }

        if (teams.getTeam1HeadCount() < maxHeadCount) {
            teamId = teams.getTeam1Id();
        } else if (teams.getTeam2HeadCount() < maxHeadCount) {
            teamId = teams.getTeam2Id();
        } else {
            throw new RuntimeException();
        }

        TeamAddUserRequestDto teamDto = TeamAddUserRequestDto.builder()
                .userId(user.getId())
                .teamId(teamId)
                .build();

        slotService.addUserInSlot(addDto);
        teamService.addUserInTeam(teamDto);
    }

    @Override
    @DeleteMapping(value = "/pingpong/match/tables/{tableId}?matchId={matchId}")
    public void removeUserInSlot(Integer matchId) {
        SlotModifyRequestDto slotDto = SlotModifyRequestDto.builder()
                .slotId(matchId)
                .build();
        slotService.removeUserInSlot(slotDto);
    }
}
