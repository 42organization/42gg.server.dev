package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameUpdateDto {
    private Integer gameId;
    private Integer team1Id;
    private Integer team2Id;
    private LocalDateTime time;
}
