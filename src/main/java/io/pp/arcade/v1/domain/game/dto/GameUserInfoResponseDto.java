package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameUserInfoResponseDto {
    Mode mode;

    LocalDateTime startTime;

    MatchTeamsInfoDto matchTeamsInfo;

    @Override
    public String toString() {
        return "GameUserInfoResponseDto{" +
                "mode=" + mode +
                ", startTime=" + startTime +
                ", myTeam=" + matchTeamsInfo.getMyTeam() +
                ", enemyTeam=" + matchTeamsInfo.getEnemyTeam() +
                '}';
    }
}
