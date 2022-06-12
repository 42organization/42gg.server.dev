package io.pp.arcade.domain.currentmatch.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchResponseDto;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class CurrentMatchImpl implements CurrentMatch {
    private final CurrentMatchService currentMatchService;
    private final UserService userService;

    @Override
    @GetMapping(value = "/match/current")
    public CurrentMatchResponseDto CurrentMatchFind(Integer userId) {
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUserId(userId);
        UserDto curUser = userService.findById(userId);
        List<String> myTeam = new ArrayList<>();
        List<String> enemyTeam = new ArrayList<>();
        Integer gameId = null;
        LocalDateTime slotTime = null;
        boolean isMatch = false;
        Integer slotId = null;
        /*
          유저의 예약 경기가 있는가?
           - 유저의 예약 경기 정보를 반환한다.
             ㄴ 유저의 경기는 5분전인가?
               - 5분 전 아님, 슬롯 시간 정보만 반환한다.(time, isMatch, slotId)
               - 5분 전, 모든 팀의 정보를 반환한다.    (time, isMatch, slotId, myteam, enemyTeam)
             ㄴ 유저의 경기는 시작되었는가?
               - 시작 됨, 관련된 모든 정보를 반환한다.  (time, isMatch, slotId, myteam, enemyTeam, gameId)
        */
        if (currentMatch != null)
        {
            SlotDto slot = currentMatch.getSlot();
            slotId = slot.getId();
            slotTime = slot.getTime();
            isMatch = currentMatch.getIsMatched();
            // 경기는 5분전이고 매치가 성사되었는가?
            if (currentMatch.getMatchImminent() && isMatch){
                TeamDto team1 = slot.getTeam1();
                TeamDto team2 = slot.getTeam2();
                TeamDto myTeamDto;
                TeamDto enemyTeamDto;

                if (curUser.equals(team1.getUser1()) || curUser.equals(team1.getUser2())) {
                    myTeamDto = team1;
                    enemyTeamDto = team2;
                } else if (curUser.equals(team2.getUser1()) || curUser.equals(team2.getUser2())) {
                    myTeamDto = team2;
                    enemyTeamDto = team1;
                } else {
                    throw new IllegalArgumentException("잘못된 요청입니다.");
                }

                List<UserDto> myTeamUsers = new ArrayList<>();
                myTeamUsers.add(myTeamDto.getUser1());
                myTeamUsers.add(myTeamDto.getUser2());
                List<UserDto> enemyTeamUsers = new ArrayList<>();
                enemyTeamUsers.add(enemyTeamDto.getUser1());
                enemyTeamUsers.add(enemyTeamDto.getUser2());

                for (UserDto user : myTeamUsers) {
                    myTeam.add(user != null ? user.getIntraId() : null);
                }
                for (UserDto user : enemyTeamUsers) {
                    enemyTeam.add(user != null ? user.getIntraId() : null);
                }
            }
            // 경기가 시작되었는가?
            if (currentMatch.getGame() != null) {
                gameId = currentMatch.getGame().getId();
            }
        }
        CurrentMatchResponseDto responseDto = CurrentMatchResponseDto.builder()
                .time(slotTime)
                .isMatched(isMatch)
                .gameId(gameId)
                .slotId(slotId)
                .myTeam(myTeam)
                .enemyTeam(enemyTeam)
                .build();
        return responseDto;
    }
}
