package io.pp.arcade.domain.slot.dto;


import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.dto.TeamDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotDto {
    private Integer id;
    private TeamDto team1;
    private TeamDto team2;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private String type;

    //entity를 dto로 바꿔주는 메서드
    public static SlotDto from(Slot slot) {
        return SlotDto.builder()
                .id(slot.getId())
                .team1(TeamDto.from(slot.getTeam1()))
                .team2(TeamDto.from(slot.getTeam2()))
                .time(slot.getTime())
                .gamePpp(slot.getGamePpp())
                .headCount(slot.getHeadCount())
                .type(slot.getType())
                .build();
    }
}
