package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class CurrentMatchUpdateRequestDto {
    private Integer currentMatchId;
    private Integer userId;
    private Integer slotId;
    private Integer gameId;
    private Boolean matchImminent;
    private Boolean isMatched;
    private Boolean isDel;
}
