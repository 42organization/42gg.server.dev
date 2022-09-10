package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankUpdateDto {
    private String intraId;
    private GameType gameType;
    private Boolean isWin;
    private Integer Ppp;

    @Override
    public String toString() {
        return "RankModifyDto{" +
                "intraId='" + intraId + '\'' +
                ", gameType=" + gameType +
                ", isWin=" + isWin +
                ", Ppp=" + Ppp +
                '}';
    }
}
