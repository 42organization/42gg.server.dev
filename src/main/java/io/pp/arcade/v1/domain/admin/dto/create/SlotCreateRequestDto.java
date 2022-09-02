package io.pp.arcade.v1.domain.admin.dto.create;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlotCreateRequestDto {
    private Integer tableId;
    private Integer team1Id;
    private Integer team2Id;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private GameType type;
}
