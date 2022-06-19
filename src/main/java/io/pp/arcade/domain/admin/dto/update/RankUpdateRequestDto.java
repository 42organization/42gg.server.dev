package io.pp.arcade.domain.admin.dto.update;

import io.pp.arcade.global.util.RacketType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RankUpdateRequestDto {
    private Integer rankId;
    private Integer userId;
    private Integer ppp;
    private Integer seasonId;
    private Integer rangking;
    private RacketType racketType;
    private Integer wins;
    private Integer losses;
}
