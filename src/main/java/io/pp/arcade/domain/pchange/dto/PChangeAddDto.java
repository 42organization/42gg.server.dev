package io.pp.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeAddRequestDto {
    private Integer gameId;
    private Integer userId;
    private Integer pppChange;
    private Integer pppResult;
}
