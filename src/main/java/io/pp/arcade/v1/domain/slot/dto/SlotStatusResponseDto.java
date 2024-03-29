package io.pp.arcade.v1.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SlotStatusResponseDto {
    List<List<SlotStatusDto>> matchBoards;

    @Override
    public String toString() {
        return "SlotStatusResponseDto{" +
                "matchBoards=" + matchBoards +
                '}';
    }
}
