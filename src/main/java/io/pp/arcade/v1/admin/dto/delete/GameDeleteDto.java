package io.pp.arcade.v1.admin.dto.delete;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameDeleteDto {
    private Integer gameId;
}
