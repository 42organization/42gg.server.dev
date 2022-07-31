package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankModifyDto {
    private String intraId;
    private GameType gameType;
    private Integer modifyStatus;
    private Integer Ppp;

    @Override
    public String toString() {
        return "RankModifyDto{" +
                "intraId='" + intraId + '\'' +
                ", gameType=" + gameType +
                ", isWin=" + modifyStatus +
                ", Ppp=" + Ppp +
                '}';
    }
}
