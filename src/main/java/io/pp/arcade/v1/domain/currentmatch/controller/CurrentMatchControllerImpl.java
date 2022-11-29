package io.pp.arcade.v1.domain.currentmatch.controller;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchResponseDto;
import io.pp.arcade.v1.domain.game.dto.GameUserInfoDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.team.TeamService;
import io.pp.arcade.v1.domain.team.dto.TeamsUserListDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class CurrentMatchControllerImpl implements CurrentMatchController {
    private final CurrentMatchService currentMatchService;
    private final TeamService teamService;
    private final TokenService tokenService;

    @Override
    @GetMapping(value = "/match/current")
    public CurrentMatchResponseDto currentMatchFind(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUser(user);;
        CurrentMatchResponseDto responseDto = getCurrentMatchResponseDto(currentMatch, user);
        return responseDto;
    }

    @PutMapping(value = "/match/current")
    public void deleteCurrentMatch(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        currentMatchService.removeCurrentMatch(CurrentMatchRemoveDto.builder()
                .user(user).build());
    }

    private CurrentMatchResponseDto getCurrentMatchResponseDto(CurrentMatchDto currentMatch, UserDto curUser) {
         /*
          유저의 예약 경기가 있는가?
           - 유저의 예약 경기 정보를 반환한다.
             ㄴ 유저의 경기는 5분전인가?
               - 5분 전 아님, 슬롯 시간 정보만 반환한다.(time, isMatch, slotId)
               - 5분 전, 모든 팀의 정보를 반환한다.    (time, isMatch, slotId, myteam, enemyTeam)
             ㄴ 유저의 경기는 시작되었는가?
               - 시작 됨, 관련된 모든 정보를 반환한다.  (time, isMatch, slotId, myteam, enemyTeam, gameId)
        */
        List<String> myTeam = new ArrayList<>();
        List<String> enemyTeam = new ArrayList<>();
        LocalDateTime slotTime = null;
        boolean isMatch = false;
        Integer slotId = null;
        Mode mode = null;

        if (currentMatch != null) {
            SlotDto slot = currentMatch.getSlot();
            slotId = slot.getId();
            slotTime = slot.getTime();
            isMatch = currentMatch.getIsMatched();
            mode = slot.getMode();
            // 경기는 5분전이고 매치가 성사되었는가?
//            if (currentMatch.getMatchImminent() && isMatch){
            if (isMatch){
                TeamsUserListDto teamsUserListDto = teamService.findUserListInTeams(slot, curUser);
                myTeam = getTeamUsersIntraIdList(teamsUserListDto.getMyTeam());
                enemyTeam = getTeamUsersIntraIdList(teamsUserListDto.getEnemyTeam());
            }
        }
        CurrentMatchResponseDto responseDto = CurrentMatchResponseDto.builder()
                .time(slotTime)
                .isMatched(isMatch)
                .slotId(slotId)
                .myTeam(myTeam)
                .enemyTeam(enemyTeam)
                .mode(mode)
                .build();
        return responseDto;
    }

    private List<String> getTeamUsersIntraIdList(List<GameUserInfoDto> infoDto) {
        List<String> teamUsers = new ArrayList<>();
        for (GameUserInfoDto dto : infoDto) {
            teamUsers.add(dto.getIntraId());
        }
        return teamUsers;
    }
}
