package io.pp.arcade.v1.domain.team.dto;

import io.pp.arcade.v1.domain.game.dto.GameUserInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TeamsUserListDto {
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
