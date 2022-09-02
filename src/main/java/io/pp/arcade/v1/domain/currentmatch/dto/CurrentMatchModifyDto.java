package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchModifyDto {
    private Integer slotId;
    private Boolean isMatched;
    private Boolean matchImminent;
    private Boolean isDel;
    private GameDto gameDto;

    @Override
    public String toString() {
        return "CurrentMatchModifyDto{" +
                "slotId=" + slotId +
                ", isMatched=" + isMatched +
                ", matchImminent=" + matchImminent +
                ", isDel=" + isDel +
                ", gameDto=" + gameDto +
                '}';
    }
}
