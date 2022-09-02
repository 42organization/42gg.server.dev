package io.pp.arcade.v1.domain.admin.dto.create;

import lombok.Getter;

@Getter
public class TeamCreateRequestDto {
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
}
