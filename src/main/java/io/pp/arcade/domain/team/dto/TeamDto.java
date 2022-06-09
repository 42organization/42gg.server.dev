package io.pp.arcade.domain.team.dto;

import io.pp.arcade.domain.team.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamDto {
    private Integer id;
    private Integer user1Id;
    private Integer user2Id;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;

    public static TeamDto from(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .user1Id(team.getUser1Id())
                .user2Id(team.getUser2Id())
                .teamPpp(team.getTeamPpp())
                .headCount(team.getHeadCount())
                .score(team.getScore())
                .win(team.getWin())
                .build();
    }
}