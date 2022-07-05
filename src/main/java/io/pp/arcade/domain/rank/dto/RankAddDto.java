package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RankAddDto {
    private Integer userId;
    private Integer ppp;
}
