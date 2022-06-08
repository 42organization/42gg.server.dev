package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.*;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addUserInTeam(TeamAddUserRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        Team team = teamRepository.findByTeamId(dto.getTeamId()).orElseThrow(RuntimeException::new);

        if (team.getUser1() == null) {
            team.setUser1(user);
            team.setTeamPpp((user.getPpp() + team.getTeamPpp()) / (team.getHeadCount() + 1));
        } else if (team.getUser2() == null) {
            team.setUser2(user);
            team.setTeamPpp((user.getPpp() + team.getTeamPpp()) / (team.getHeadCount() + 1));
        }
        team.setHeadCount(team.getHeadCount() + 1);
    }

    @Transactional
    public void removeUserInTeam(TeamRemoveUserRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        Team team = teamRepository.findByTeamId(dto.getTeamId()).orElseThrow(RuntimeException::new);
        if (team.getUser1().getId().equals(dto.getUserId())) {
            team.setUser1(null);
        } else if (team.getUser2().getId().equals(dto.getUserId())){
            team.setUser2(null);
        }
        team.setHeadCount(team.getHeadCount() - 1);
    }

    @Transactional
    public TeamDto findTeamByTeamId(String teamId) {
        Team team = teamRepository.findByTeamId(teamId).orElseThrow(RuntimeException::new);
        TeamDto dto = TeamDto.from(team);
        return dto;
    }

    @Transactional
    public TeamListResponseDto findTeamUserCountByTeamList(TeamListRequestDto listDto) {
        String team1Id = listDto.getTeam1Id();
        String team2Id = listDto.getTeam2Id();

        Team team1 = teamRepository.findByTeamId(team1Id).orElseThrow(RuntimeException::new);
        Team team2 = teamRepository.findByTeamId(team2Id).orElseThrow(RuntimeException::new);
        return TeamListResponseDto.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .team1HeadCount(team1.getHeadCount())
                .team2HeadCount(team2.getHeadCount())
                .build();
    }
}
