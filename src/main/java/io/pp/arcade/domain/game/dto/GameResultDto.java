package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameResultDto {
    private Integer gameId;
    private GameType type;
    private StatusType status;
    private LocalDateTime time;
    private GameTeamDto team1;
    private GameTeamDto team2;
}
