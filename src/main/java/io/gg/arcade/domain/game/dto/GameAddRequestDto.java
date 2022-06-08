package io.gg.arcade.domain.game.dto;

import io.gg.arcade.domain.game.entity.Game;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameAddRequestDto {
    private String team1Id;
    private String team2Id;

    public Game toEntity(){
        return Game.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .team1Win(false)
                .team2Win(false)
                .build();
    }
}
