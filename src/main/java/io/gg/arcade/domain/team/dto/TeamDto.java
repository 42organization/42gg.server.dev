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
    private String teamId;
    private UserDto user1;
    private UserDto user2;
    private Integer headCount;
    private Integer teamPpp;

    public static TeamDto from(Team team){
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .user1(UserDto.from(team.getUser1()))
                .user2(UserDto.from(team.getUser2()))
                .headCount(team.getHeadCount())
                .teamPpp(team.getTeamPpp())
                .build();
    }
}
