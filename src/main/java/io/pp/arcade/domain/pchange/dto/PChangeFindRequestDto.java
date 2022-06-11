package io.pp.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PChangeFindRequestDto {
    private Integer GameId;
}
