package io.pp.arcade.domain.admin.dto.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameCreateDto {
    private Integer slotId;
    private Integer seasonId;
    private String status;
}
