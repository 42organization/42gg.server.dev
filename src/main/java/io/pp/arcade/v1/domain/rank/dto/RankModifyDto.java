package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
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
                ", modifyStatus=" + modifyStatus +
                ", Ppp=" + Ppp +
                '}';
    }
}
