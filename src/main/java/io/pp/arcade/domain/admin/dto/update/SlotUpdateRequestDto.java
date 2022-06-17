package io.pp.arcade.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotUpdateRequestDto {
    private Integer id;
    private Integer gamePpp;
    private Integer headCount;
    private String type;
}
