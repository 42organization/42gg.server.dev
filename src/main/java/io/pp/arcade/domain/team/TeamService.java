package io.pp.arcade.domain.team;


import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.dto.*;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamDto findById(Integer id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return TeamDto.from(team);
    }

    @Transactional
    public void addUserInTeam(TeamAddUserDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));

        if (team.getUser1() == null) {
            team.setUser1(user);
        } else {
            team.setUser2(user);
        }
        team.setTeamPpp((user.getPpp() + team.getTeamPpp()) / (team.getHeadCount() + 1));
        team.setHeadCount(team.getHeadCount() + 1);
    }

    @Transactional
    public void removeUserInTeam(TeamRemoveUserDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));

        if (team.getUser1().equals(user)) {
            team.setUser1(null);
        } else {
            team.setUser2(null);
        } // id가 user1이나 user2 둘중 하나로 validation 거쳐서 들어옴
        Integer headCountResult = team.getHeadCount() - 1; // entity라 반영이 안되어서 미리 뺀 값을 써줘야함
        if (headCountResult == 0) {
            team.setTeamPpp(0);
        } else {
            team.setTeamPpp((team.getTeamPpp() * team.getHeadCount() - user.getPpp()) / headCountResult);
        }
        team.setHeadCount(headCountResult);
    }


    @Transactional
    public void saveGameResultInTeam(TeamSaveGameResultDto dto) {
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));

        team.setScore(dto.getScore());
        team.setWin(dto.getWin());
    }

    @Transactional
    public void modifyGameResultInTeam(TeamModifyGameResultDto dto) {
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));

        team.setScore(dto.getScore());
        team.setWin(dto.getWin());
    }

    public TeamPosDto getTeamPosNT(UserDto curUser, TeamDto team1, TeamDto team2) {
        TeamDto myTeamDto;
        TeamDto enemyTeamDto;

        if (curUser.equals(team1.getUser1()) || curUser.equals(team1.getUser2())) {
            myTeamDto = team1;
            enemyTeamDto = team2;
        } else if (curUser.equals(team2.getUser1()) || curUser.equals(team2.getUser2())) {
            myTeamDto = team2;
            enemyTeamDto = team1;
        } else {
            throw new IllegalArgumentException("?");
        }
        TeamPosDto dto = TeamPosDto.builder()
                .myTeam(myTeamDto)
                .enemyTeam(enemyTeamDto)
                .build();
        return dto;
    }
    /* 아이디를 통한 팀 추가 */
}
