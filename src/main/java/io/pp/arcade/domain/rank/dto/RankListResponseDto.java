package io.pp.arcade.domain.rank.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RankListResponseDto {
    private Integer myRank;
    private Integer currentPage;
    private Integer totalPage;
    private List<RankUserDto> rankList;
}
