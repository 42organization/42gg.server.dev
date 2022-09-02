package io.pp.arcade.v1.domain.slot.dto;


import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotDto {
    private Integer id;
    private Integer tableId;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private GameType type;
    private Mode mode;

    //entity를 dto로 바꿔주는 메서드
    public static SlotDto from(Slot slot) {
        return SlotDto.builder()
                .id(slot.getId())
                .tableId(slot.getTableId())
                .time(slot.getTime())
                .gamePpp(slot.getGamePpp())
                .headCount(slot.getHeadCount())
                .type(slot.getType())
                .mode(slot.getMode())
                .build();
    }

    @Override
    public String toString() {
        return "SlotDto{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", time=" + time +
                ", gamePpp=" + gamePpp +
                ", headCount=" + headCount +
                ", type=" + type +
                ", mode=" + (mode != null ? mode.getCode() : null) +
                '}';
    }
}
