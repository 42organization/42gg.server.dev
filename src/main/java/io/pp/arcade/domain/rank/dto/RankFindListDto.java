package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RankFindListDto {
    private Integer currentPage;
    private Integer totalPage;
    private List<RankUserDto> rankList;
}
