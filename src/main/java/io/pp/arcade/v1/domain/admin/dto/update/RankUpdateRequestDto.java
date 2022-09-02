package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class RankUpdateRequestDto {
    private Integer rankId;
    private Integer ppp;
    private Integer wins;
    private Integer losses;
}
