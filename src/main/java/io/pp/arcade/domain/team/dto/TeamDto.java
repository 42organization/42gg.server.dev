package io.pp.arcade.domain.team.dto;

import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TeamDto {
    private Integer id;
    private UserDto user1;
    private UserDto user2;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;

    public static TeamDto from(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .user1(UserDto.from(team.getUser1()))
                .user2(UserDto.from(team.getUser2()))
                .teamPpp(team.getTeamPpp())
                .headCount(team.getHeadCount())
                .score(team.getScore())
                .win(team.getWin())
                .build();
    }
}