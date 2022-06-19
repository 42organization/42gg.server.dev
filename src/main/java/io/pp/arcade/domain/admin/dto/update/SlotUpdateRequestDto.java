package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotUpdateRequestDto {
    private Integer id;
    private Integer gamePpp;
    private Integer headCount;
    private GameType type;
}
