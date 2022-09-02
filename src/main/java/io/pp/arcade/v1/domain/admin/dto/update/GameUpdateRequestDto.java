package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class GameUpdateRequestDto {
    private Integer gameId;
    private Integer team1Score;
    private Integer team2Score;
}
