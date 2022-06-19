package io.pp.arcade.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameUpdateDto {
    private Integer gameId;
    private Integer slotId;
    private Integer team1Id;
    private Integer team2Id;
    private String type;
    private LocalDateTime time;
    private Integer seasonId;
    private String status;
}
