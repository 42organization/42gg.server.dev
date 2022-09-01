package io.pp.arcade.domain.game.dto;

import io.pp.arcade.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
