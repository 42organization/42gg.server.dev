package io.gg.arcade.domain.pchange.dto;

import io.gg.arcade.domain.pchange.entity.PChange;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PchangeAddRequestDto {
    Integer gameId;
    Integer userId;
    Integer pppChange;
    Integer pppResult;

    public PChange toEnity(Integer gameId, Integer userId, Integer pppChange, Integer pppResult) {
        return (
                PChange.builder()
                .gameId(gameId)
                .userId(userId)
                .pppChange(pppChange)
                .pppResult(pppResult)
                .build()
        );
    }
}
