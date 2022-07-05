package io.pp.arcade.domain.slot.dto;


import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.SlotStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class SlotDto {
    private Integer id;
    private Integer tableId;
    private TeamDto team1;
    private TeamDto team2;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private GameType type;

    //entity를 dto로 바꿔주는 메서드
    public static SlotDto from(Slot slot) {
        return SlotDto.builder()
                .id(slot.getId())
                .tableId(slot.getTableId())
                .team1(TeamDto.from(slot.getTeam1()))
                .team2(TeamDto.from(slot.getTeam2()))
                .time(slot.getTime())
                .gamePpp(slot.getGamePpp())
                .headCount(slot.getHeadCount())
                .type(slot.getType())
                .build();
    }
}
