package io.pp.arcade.domain.game.dto;

import io.pp.arcade.domain.team.dto.TeamDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameDto {
    private Integer id;
    private TeamDto team1;
    private TeamDto team2;
    private String type;
    private LocalDateTime time;
    private Integer season;
    private String status;
}
