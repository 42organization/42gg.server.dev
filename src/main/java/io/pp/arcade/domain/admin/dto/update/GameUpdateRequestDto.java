package io.pp.arcade.domain.admin.dto.update;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameUpdateRequestDto {
    private Integer gameId;
    private Integer slotId;
    private Integer team1Id;
    private Integer team2Id;
    private String type;
    private LocalDateTime time;
    private Integer seasonId;
    private String status;
}
