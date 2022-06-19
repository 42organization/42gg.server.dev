package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameUpdateRequestDto {
    private Integer gameId;
    private Integer slotId;
    private Integer team1Id;
    private Integer team2Id;
    private GameType type;
    private LocalDateTime time;
    private Integer seasonId;
    private StatusType status;
}
