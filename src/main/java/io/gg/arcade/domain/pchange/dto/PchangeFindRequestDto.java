package io.gg.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PchangeFindRequestDto {
    Integer gameId;
    Integer userId;
}
