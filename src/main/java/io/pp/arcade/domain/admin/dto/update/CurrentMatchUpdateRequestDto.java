package io.pp.arcade.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class CurrentMatchUpdateRequestDto {
    private Integer currenMatchId;
    private Integer slotId;
    private Integer userId;
    private Integer gameId;
    private Boolean matchImminent;
    private Boolean isMatched;
}
