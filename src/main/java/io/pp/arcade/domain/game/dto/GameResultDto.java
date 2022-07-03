package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
public class GameResultDto {
    private Integer gameId;
    private String type;
    private String status;
    private LocalDateTime time;
    private GameTeamDto team1;
    private GameTeamDto team2;

    @Builder
    public GameResultDto(Integer gameId, GameType type, StatusType status, LocalDateTime time, GameTeamDto team1, GameTeamDto team2) {
        this.gameId = gameId;
        this.type = type.getCode();
        this.status = status.getCode();
        this.time = time;
        this.team1 = team1;
        this.team2 = team2;
    }
}
