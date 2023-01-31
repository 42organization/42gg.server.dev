package io.pp.arcade.v1.admin.dto.create;

import io.pp.arcade.v1.global.type.StatusType;
import lombok.Getter;

@Getter
public class GameCreateRequestDto {
    private Integer slotId;
    private Integer seasonId;
    private StatusType status;
}
