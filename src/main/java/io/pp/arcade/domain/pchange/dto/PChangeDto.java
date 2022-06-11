package io.pp.arcade.domain.pchange.dto;

import io.pp.arcade.domain.pchange.PChange;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeDto {
    private Integer gameId;
    private Integer userId;
    private Integer pppChange;
    private Integer pppResult;

    public static PChangeDto from(PChange pChange){
        return PChangeDto.builder()
                .gameId(pChange.getGameId())
                .userId(pChange.getUserId())
                .pppChange(pChange.getPppChange())
                .pppResult(pChange.getPppResult())
                .build();
    }
}
