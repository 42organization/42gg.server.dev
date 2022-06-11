package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.game.Game;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameResultDto {
    private Integer gameId;
    private String type;
    private String status;
    private LocalDateTime time;
    private GameTeamDto team1;
    private GameTeamDto team2;

}
