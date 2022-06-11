package io.pp.arcade.domain.currentmatch.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchResponseDto;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        List<String> myTeam = new ArrayList<>();
        List<String> enemyTeam = new ArrayList<>();

        TeamDto team1 = currentMatch.getSlot().getTeam1();
        TeamDto team2 = currentMatch.getSlot().getTeam2();
        TeamDto myTeamDto;
        TeamDto enemyTeamDto;

        UserDto curUser = userService.findById(userId);
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

        for (UserDto user : myTeamUsers){
                myTeam.add(user != null ? user.getIntraId() : null);
        }
        for (UserDto user : enemyTeamUsers){
                myTeam.add(user != null ? user.getIntraId() : null);
        }

        CurrentMatchResponseDto responseDto = CurrentMatchResponseDto.builder()
                .time(currentMatch.getSlot().getTime())
                .isMatched(currentMatch.getIsMatched())
                .gameId(currentMatch.getGameId())
                .slotId(currentMatch.getSlot().getId())
                .myTeam(myTeam)
                .enemyTeam(enemyTeam)
                .build();
        return responseDto;
    }
}
