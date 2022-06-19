package io.pp.arcade.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotUpdateDto {
    private Integer slotId;
    private Integer gamePpp;
    private Integer headCount;
    private String type;
}
