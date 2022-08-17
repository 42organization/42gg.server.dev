package io.pp.arcade.domain.currentmatch.dto;

import io.pp.arcade.domain.game.dto.GameDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchModifyDto {
    private Integer slotId;
    private Boolean isMatched;
    private Boolean matchImminent;
    private GameDto gameDto;

    @Override
    public String toString() {
        return "CurrentMatchModifyDto{" +
                "slotId=" + slotId +
                ", isMatched=" + isMatched +
                ", matchImminent=" + matchImminent +
                ", gameDto=" + gameDto +
                '}';
    }
}
