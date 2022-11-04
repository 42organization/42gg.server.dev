package io.pp.arcade.v1.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameExpAndPppResultDto {
    private Integer beforeExp;
    private Integer beforeMaxExp;
    private Integer beforeLevel;
    private Integer increasedExp;
    private Integer increasedLevel;
    private Integer afterMaxExp;
    private Integer pppChange;
    private Integer pppResult;
}
