package io.pp.arcade.domain.team.dto;

import io.pp.arcade.domain.game.dto.GameUserInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TeamPosDto {
    List<GameUserInfoDto> myTeam;
    List<GameUserInfoDto> enemyTeam;

    @Override
    public String toString() {
        return "TeamPosDto{" +
                "myTeam=" + myTeam +
                ", enemyTeam=" + enemyTeam +
                '}';
    }
}
