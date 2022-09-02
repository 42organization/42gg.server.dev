package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankFindDto {
    private String intraId;
    private GameType gameType;

    @Override
    public String toString() {
        return "RankFindDto{" +
                "intraId='" + intraId + '\'' +
                ", gameType=" + gameType +
                '}';
    }
}
