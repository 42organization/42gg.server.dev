package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.*;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addUserInTeam(TeamAddUserRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        teamRepository.save(
                Team.builder()
                .teamId(dto.getTeamId())
                .user(user)
                .build()
        );
    }

    @Transactional
    public void removeUserInTeam(TeamRemoveUserRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        teamRepository.deleteByUserAndTeamId(user, dto.getTeamId());
    }

    @Transactional
    public TeamListResponseDto findTeamUserCountByTeamList(TeamListRequestDto listDto) {
        String team1Id = listDto.getTeam1Id();
        String team2Id = listDto.getTeam1Id();

        List<Team> listTeam = teamRepository.findTeamByTeamIdOrTeamId(team1Id, team2Id);
        return TeamListResponseDto.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .team1HeadCount(Collections.frequency(listTeam, team1Id))
                .team2HeadCount(Collections.frequency(listTeam, team2Id))
                .build();
    }

    @Transactional
    public List<TeamDto> findUserListByTeamId(String teamId){
        List<Team> teamList = teamRepository.findByTeamId(teamId);
        List<TeamDto> teamDtoList = new ArrayList<TeamDto>();
        teamList.stream().forEach(team->{
            teamDtoList.add(TeamDto.from(team));
        });
        return teamDtoList;
    }

    @Transactional
    public List<UserDto> findTeamIdByUserId(String userId) {
        teamRepository.findTeamIdByUserId(userId);
        return null;
    }
}
