package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.team.entity.Team;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class TeamDto {
    String teamId;
    UserDto userDto;

    public static TeamDto from(Team team){
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .userDto(UserDto.from(team.getUser()))
                .build();
    }
}
