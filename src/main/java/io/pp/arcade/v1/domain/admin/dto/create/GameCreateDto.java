package io.pp.arcade.v1.domain.admin.dto.create;

import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameCreateDto {
    private Integer slotId;
    private Integer team1Id;
    private Integer team2Id;
    private GameType type;
    private LocalDateTime time;
    private Integer seasonId;
    private StatusType status;
}
