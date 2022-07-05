package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class RankSaveAllDto {
    private List<RankRedisDto> rankRedisDtos;
    private Integer seasonId;
}
