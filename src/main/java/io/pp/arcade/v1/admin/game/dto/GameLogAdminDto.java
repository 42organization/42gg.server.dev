package io.pp.arcade.v1.admin.game.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class GameLogAdminDto {
    private Integer gameId;
    private LocalDateTime startAt;
    private String playTime;
    private String mode;
    private GameTeamAdminDto team1;
    private GameTeamAdminDto team2;
}
