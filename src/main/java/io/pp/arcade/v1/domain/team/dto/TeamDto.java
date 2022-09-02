package io.pp.arcade.v1.domain.team.dto;

import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamDto {
    private Integer id;
    private UserDto user1;
    private UserDto user2;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;
    private SlotDto slot;

    public static TeamDto from(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .user1(null)
                .user2(null)
                .teamPpp(team.getTeamPpp())
                .headCount(team.getHeadCount())
                .score(team.getScore())
                .win(team.getWin())
                .slot(SlotDto.from(team.getSlot()))
                .build();
    }

    @Override
    public String toString() {
        return "TeamDto{" +
                "id=" + id +
                ", teamPpp=" + teamPpp +
                ", headCount=" + headCount +
                ", score=" + score +
                ", win=" + win +
                ", slot=" + slot +
                '}';
    }
}