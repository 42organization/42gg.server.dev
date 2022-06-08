package io.gg.arcade.domain.game.dto;

import io.gg.arcade.domain.game.entity.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Builder
public class GameDto {
    Integer id;
    String team1Id;
    String team2Id;
    Boolean team1Win;
    Integer team1Score;
    Boolean team2Win;
    Integer team2Score;


    public static GameDto from(Game game) {
        return GameDto.builder()
        .id(game.getId())
        .team1Id(game.getTeam1Id())
        .team2Id(game.getTeam2Id())
        .team1Win(game.getTeam1Win())
        .team2Win(game.getTeam2Win())
        .team1Score(game.getTeam1Score())
        .team2Score(game.getTeam2Score())
        .build();
    }
}
