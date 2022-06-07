package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SlotAddRequestDto {
    String team1Id;
    String team2Id;
    LocalDateTime time;
}
