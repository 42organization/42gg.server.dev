package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.game.dto.GameInfoDto;
import io.gg.arcade.domain.team.dto.TeamRequestDto;
import io.gg.arcade.domain.team.dto.TeamResponseDto;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.global.utils.EloRating;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team addUserInTeam(TeamRequestDto teamDto) {
        Team team = Team.builder()
                .teamId(teamDto.getTeamId())
                .user(teamDto.getUser())
                .isWin(null)
                .score(0)
                .pppChange(0)
                .pppResult(0)
                .build();
        return teamRepository.save(team);
    }

    @Override
    public void modifyTeamAfterGame(TeamRequestDto teamDto, GameInfoDto gameInfoDto) {
        List<Team> teams = teamRepository.findByTeamId(teamDto.getTeamId());
        Boolean isWin;
        Integer score;
        Integer teamPpp;
        Integer opponentPpp;
        if (teamDto.getTeamId().equals(gameInfoDto.getTeam1Id())) {
            isWin = gameInfoDto.getTeam1Score() > gameInfoDto.getTeam2Score();
            score = gameInfoDto.getTeam1Score();
            teamPpp = gameInfoDto.getTeam1Ppp();
            opponentPpp = gameInfoDto.getTeam2Ppp();
        } else {
            isWin = gameInfoDto.getTeam2Score() > gameInfoDto.getTeam1Score();
            score = gameInfoDto.getTeam2Score();
            teamPpp = gameInfoDto.getTeam2Ppp();
            opponentPpp = gameInfoDto.getTeam1Ppp();
        }
        for (Team team : teams) {
            team.setIsWin(isWin);
            team.setScore(score);
            team.setPppChange(EloRating.pppChange(teamPpp, opponentPpp, isWin));
            team.setPppResult(42); // 아직 랭크가 담긴 곳이 없다
        }
    }

    @Override
    public TeamResponseDto findTeam(TeamRequestDto teamDto) {
        TeamResponseDto responseDto = new TeamResponseDto();
        List<Team> teams = teamRepository.findByTeamId(teamDto.getTeamId());
        responseDto.setHeadCount(teams.size());
        responseDto.setTeams(teams);
        return responseDto;
    }

    @Override
    public void removeUserInTeam(TeamRequestDto teamDto) {
        teamRepository.deleteByUser(teamDto.getUser());
    }
}
