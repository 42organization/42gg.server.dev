package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class SeasonUpdateDto {
    private Integer id;
    private Integer startPpp;
    private Integer pppGap;
}