package io.pp.arcade.v1.domain.currentmatch.dto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchModifyDto {
    private SlotDto slot;
    private Boolean isMatched;
    private Boolean matchImminent;
    private Boolean isDel;

    @Override
    public String toString() {
        return "CurrentMatchModifyDto{" +
                "slot=" + slot.toString() +
                ", isMatched=" + isMatched +
                ", matchImminent=" + matchImminent +
                ", isDel=" + isDel +
                '}';
    }
}
