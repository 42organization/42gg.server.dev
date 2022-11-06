package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameUserInfoResponseDto {
    private String mode;

    private Integer gameId;

    private LocalDateTime startTime;

    private MatchTeamsInfoDto matchTeamsInfo;
    private Boolean scoreExist;


    @Override
    public String toString() {
        return "GameUserInfoResponseDto{" +
                "mode=" + mode +
                ", gameId=" + gameId +
                ", startTime=" + startTime +
                ", myTeam=" + matchTeamsInfo.getMyTeam() +
                ", enemyTeam=" + matchTeamsInfo.getEnemyTeam() +
                '}';
    }
}
