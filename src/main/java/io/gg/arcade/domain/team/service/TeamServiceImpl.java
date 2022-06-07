package io.gg.arcade.domain.team.service;

import io.gg.arcade.domain.team.dto.TeamAddUserRequestDto;
import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.team.repository.TeamRepository;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    @Transactional
    public void removeUserInTeam() {

    }
}
