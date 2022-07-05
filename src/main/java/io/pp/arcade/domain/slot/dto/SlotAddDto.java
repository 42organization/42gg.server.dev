package io.pp.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class SlotAddDto {
    private LocalDateTime time;
    private Integer tableId;
}
