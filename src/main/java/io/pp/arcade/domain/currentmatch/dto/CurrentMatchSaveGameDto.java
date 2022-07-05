package io.pp.arcade.domain.currentmatch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CurrentMatchSaveGameDto {
    private Integer gameId;
    private Integer userId;
}
